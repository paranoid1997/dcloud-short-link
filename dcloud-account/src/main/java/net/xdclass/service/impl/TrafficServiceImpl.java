package net.xdclass.service.impl;

import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.extern.slf4j.Slf4j;
import net.xdclass.config.RabbitMQConfig;
import net.xdclass.constant.RedisKey;
import net.xdclass.controller.request.TrafficPageRequest;
import net.xdclass.controller.request.UseTrafficRequest;
import net.xdclass.enums.BizCodeEnum;
import net.xdclass.enums.EventMessageType;
import net.xdclass.enums.TaskStateEnum;
import net.xdclass.exception.BizException;
import net.xdclass.feign.ProductFeignService;
import net.xdclass.feign.ShortLinkFeignService;
import net.xdclass.interceptor.LoginInterceptor;
import net.xdclass.manager.TrafficManager;
import net.xdclass.manager.TrafficTaskManager;
import net.xdclass.model.EventMessage;
import net.xdclass.model.LoginUser;
import net.xdclass.model.TrafficDO;
import net.xdclass.model.TrafficTaskDO;
import net.xdclass.service.TrafficService;
import net.xdclass.util.JsonData;
import net.xdclass.util.JsonUtil;
import net.xdclass.util.TimeUtil;
import net.xdclass.vo.ProductVO;
import net.xdclass.vo.TrafficVO;
import net.xdclass.vo.UseTrafficVO;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author 刘森飚
 * @since 2023-02-06
 */


@Service
@Slf4j
public class TrafficServiceImpl implements TrafficService {

    @Autowired
    private TrafficManager trafficManager;

    @Autowired
    private ProductFeignService productFeignService;

    @Autowired
    private RedisTemplate<Object,Object> redisTemplate;

    @Autowired
    private TrafficTaskManager trafficTaskManager;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RabbitMQConfig rabbitMQConfig;

    @Autowired
    private ShortLinkFeignService shortLinkFeignService;




    /**
     * 处理流量消息
     * @param eventMessage
     */
    @Override
    @Transactional(rollbackFor = Exception.class,propagation = Propagation.REQUIRED)
    public void handleTrafficMessage(EventMessage eventMessage) {
        Long accountNo = eventMessage.getAccountNo();
        String messageType = eventMessage.getEventMessageType();
        if (EventMessageType.PRODUCT_ORDER_PAY.name().equalsIgnoreCase(messageType)) {
            //付费流量包
            //订单已经支付，新增流量
            String content = eventMessage.getContent();
            Map<String, Object> orderInfoMap = JsonUtil.json2Obj(content, Map.class);
            //还原订单商品信息
            String outTradeNo = (String) orderInfoMap.get("outTradeNo");
            Integer buyNum = (Integer) orderInfoMap.get("buyNum");
            String productStr = (String) orderInfoMap.get("product");
            ProductVO productVO = JsonUtil.json2Obj(productStr, ProductVO.class);
            log.info("商品信息:{}", productVO);
            //流量包有效期
            LocalDateTime expiredDateTime = LocalDateTime.now().plusDays(productVO.getValidDay());
            Date date = Date.from(expiredDateTime.atZone(ZoneId.systemDefault()).toInstant());
            //构建流量包对象
            TrafficDO trafficDO = TrafficDO.builder()
                    .accountNo(accountNo)
                    .dayLimit(productVO.getDayTimes() * buyNum)
                    .dayUsed(0)
                    .totalLimit(productVO.getTotalTimes())
                    .pluginType(productVO.getPluginType())
                    .level(productVO.getLevel())
                    .productId(productVO.getId())
                    .outTradeNo(outTradeNo)
                    .expiredDate(date).build();

            int rows = trafficManager.add(trafficDO);
            log.info("消费消息新增流量包:rows={},trafficDO={}", rows, trafficDO);
            //新增流量包，应该删除这个key
            String totalTrafficTimesKey = String.format(RedisKey.DAY_TOTAL_TRAFFIC, accountNo);
            redisTemplate.delete(totalTrafficTimesKey);
        } else if (EventMessageType.TRAFFIC_FREE_INIT.name().equalsIgnoreCase(messageType)) {
            //发放免费流量包
            Long productId = Long.valueOf(eventMessage.getBizId());
            JsonData jsonData = productFeignService.detail(productId);
            //这个方法是把json转成vo类
            ProductVO productVO = jsonData.getData(new TypeReference<ProductVO>() {
            });
            //构建流量包对象
            TrafficDO trafficDO = TrafficDO.builder()
                    .accountNo(accountNo)
                    .dayLimit(productVO.getDayTimes())
                    .dayUsed(0)
                    .totalLimit(productVO.getTotalTimes())
                    .pluginType(productVO.getPluginType())
                    .level(productVO.getLevel())
                    .productId(productVO.getId())
                    .outTradeNo("free_init")
                    .expiredDate(new Date())
                    .build();
            trafficManager.add(trafficDO);
        } else if (EventMessageType.TRAFFIC_USED.name().equalsIgnoreCase(messageType)) {
            //流量包使用，检查是否成功使用
            //检查task是否存在
            //检查短链是否成功
            //如果不成功，则恢复流量包
            //删除task (也可以更新task状态，定时删除就行)
            Long trafficTaskId = Long.valueOf(eventMessage.getBizId());
            TrafficTaskDO trafficTaskDO = trafficTaskManager.findByIdAndAccountNo(trafficTaskId, accountNo);
            //非空且锁定
            if (trafficTaskDO != null && trafficTaskDO.getLockState().equalsIgnoreCase(TaskStateEnum.LOCK.name())) {
                //远程调用，查看短链码是否存在
                JsonData jsonData = shortLinkFeignService.check(trafficTaskDO.getBizId());
                if (jsonData.getCode() != 0) {
                    log.error("创建短链失败，流量包回滚");
                    String useDateStr =  TimeUtil.format(trafficTaskDO.getGmtCreate(),"yyyy-MM-dd");
                    trafficManager.releaseUsedTimes(accountNo,trafficTaskDO.getTrafficId(),1,useDateStr);
                    //恢复流量包，应该删除这个key（也可以让这个key递增）
                    String totalTrafficTimesKey = String.format(RedisKey.DAY_TOTAL_TRAFFIC,accountNo);
                    redisTemplate.delete(totalTrafficTimesKey);
                }
                //多种方式处理task，不立刻删除，可以更新状态，然后定时删除也行
                trafficTaskManager.deleteByIdAndAccountNo(trafficTaskId, accountNo);
            }
        }
    }




    /**
     * 分页查看可用的流量包
     * @param request
     * @return
     */
    @Override
    public Map<String, Object> pageAvailable(TrafficPageRequest request) {
        int size = request.getSize();
        int page = request.getPage();
        LoginUser loginUser = LoginInterceptor.threadLocal.get();
        IPage<TrafficDO> trafficDOIPage = trafficManager.pageAvailable(page, size, loginUser.getAccountNo());
        //获取流量包列表
        List<TrafficDO> records = trafficDOIPage.getRecords();
        List<TrafficVO> trafficVOList = records.stream().map(obj -> beanProcess(obj)).collect(Collectors.toList());
        Map<String, Object> pageMap = new HashMap<>(3);
        pageMap.put("total_record", trafficDOIPage.getTotal());
        pageMap.put("total_page",trafficDOIPage.getPages());
        pageMap.put("current_data",trafficVOList);
        return pageMap;
    }



    /**
     *  查询流量包详情
     * @param trafficId
     * @return
     */
    @Override
    public TrafficVO detail(long trafficId) {
        LoginUser loginUser = LoginInterceptor.threadLocal.get();
        TrafficDO trafficDO = trafficManager.findByIdAndAccountNo(trafficId, loginUser.getAccountNo());
        return beanProcess(trafficDO);
    }



    /**
     * 删除过期流量包
     * @return
     */
    @Override
    public boolean deleteExpireTraffic() {
        return trafficManager.deleteExpireTraffic();
    }




    /**
     *  查询用户全部可用流量包
     *  遍历用户可用流量包
     *    判断是否更新-用日期判断
     *      没更新的流量包后加入【待更新集合】中
     *        增加【今天剩余可用总次数】
     *      已经更新的判断是否超过当天使用次数
     *        如果没超过则增加【今天剩余可用总次数】
     *        超过则忽略
     *
     * * 更新用户今日流量包相关数据
     * * 扣减使用的某个流量包使用次数
     * @param trafficRequest
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class,propagation = Propagation.REQUIRED)
    public JsonData reduce(UseTrafficRequest trafficRequest) {
        Long accountNo = trafficRequest.getAccountNo();
        //处理流量包，筛选出未更新流量包，当前使用的流量包
        UseTrafficVO useTrafficVO = processTrafficList(accountNo);
        log.info("今天可用总次数:{},当前使用流量包:{}",
                useTrafficVO.getDayTotalLeftTimes(),useTrafficVO.getCurrentTrafficDO());
        //如果没有可用流量包
        if(useTrafficVO.getCurrentTrafficDO() == null){
            return JsonData.buildResult(BizCodeEnum.TRAFFIC_REDUCE_FAIL);
        }
        log.info("待更新流量包列表:{}",useTrafficVO.getUnUpdatedTrafficIds());
        if(useTrafficVO.getUnUpdatedTrafficIds().size() > 0){
            //更新今日流量包
            trafficManager.batchUpdateUsedTimes(accountNo,useTrafficVO.getUnUpdatedTrafficIds());
        }
        //先更新，再扣减当前使用的流量包
        //扣减流量包就是意味着增加了一次当天流量包的使用次数
        int rows = trafficManager.addDayUsedTimes(
                accountNo,useTrafficVO.getCurrentTrafficDO().getId(),1);

        //6-1保存task任务
        TrafficTaskDO trafficTaskDO = TrafficTaskDO.builder()
                .accountNo(accountNo)
                .bizId(trafficRequest.getBizId())
                .useTimes(1)
                .trafficId(useTrafficVO.getCurrentTrafficDO().getId())
                .lockState(TaskStateEnum.LOCK.name())
                .build();

        trafficTaskManager.add(trafficTaskDO);

        if(rows != 1){
            throw new BizException(BizCodeEnum.TRAFFIC_REDUCE_FAIL);
        }

        //往redis设置下总流量包次数，短链服务那边递减即可； 如果有新增流量包，则删除这个key
        long leftSeconds = TimeUtil.getRemainSecondsOneDay(new Date());
        String totalTrafficTimesKey = String.format(RedisKey.DAY_TOTAL_TRAFFIC,accountNo);
        redisTemplate.opsForValue().set(totalTrafficTimesKey,
                useTrafficVO.getDayTotalLeftTimes() - 1,leftSeconds, TimeUnit.SECONDS);

        //6-3发送MQ消息
        EventMessage trafficUseEventMessage = EventMessage.builder()
                .accountNo(accountNo)
                .bizId(trafficTaskDO.getId() + "")
                .eventMessageType(EventMessageType.TRAFFIC_USED.name())
                .build();

        //发送延迟消息，用于异常回滚
        rabbitTemplate.convertAndSend(rabbitMQConfig.getTrafficEventExchange(),
                rabbitMQConfig.getTrafficReleaseDelayRoutingKey(),trafficUseEventMessage);

        return JsonData.buildSuccess();
    }



    /**
     * 处理流量包
     * 筛选出未更新流量包
     * 当前使用的流量包
     * @param accountNo
     * @return
     */
    private UseTrafficVO processTrafficList(Long accountNo) {
        //全部流量包
        List<TrafficDO> list = trafficManager.selectAvailableTraffics(accountNo);
        if(list == null || list.size() == 0){ throw  new BizException(BizCodeEnum.TRAFFIC_EXCEPTION); }
        //天剩余可用总次数
        Integer dayTotalLeftTimes = 0;
        //当前使用
        TrafficDO currentTrafficDO = null;
        //没过期，但是今天没更新的流量包id列表
        List<Long> unUpdatedTrafficIds = new ArrayList<>();
        //今天日期
        String todayStr = TimeUtil.format(new Date(),"yyyy-MM-dd");
        //遍历用户所有可用流量包
        for(TrafficDO trafficDO : list){
            String trafficUpdateDate = TimeUtil.format(trafficDO.getGmtModified(),"yyyy-MM-dd");
            if(todayStr.equalsIgnoreCase(trafficUpdateDate)){
                //已经更新  天剩余可用总次数 = 总次数 - 已用
                int dayLeftTimes = trafficDO.getDayLimit() - trafficDO.getDayUsed();
                dayTotalLeftTimes = dayTotalLeftTimes + dayLeftTimes;
                //选取当次使用流量包
                //currentTrafficDO == null表示当前流量包没有在被人使用
                if(dayLeftTimes > 0 && currentTrafficDO == null){
                    currentTrafficDO = trafficDO;
                }
            }else {
                //未更新
                dayTotalLeftTimes = dayTotalLeftTimes + trafficDO.getDayLimit();
                //记录未更新的流量包
                unUpdatedTrafficIds.add(trafficDO.getId());
                //选取当次使用流量包
                if(currentTrafficDO == null){
                    currentTrafficDO = trafficDO;
                }
            }
        }
        UseTrafficVO useTrafficVO = new UseTrafficVO(dayTotalLeftTimes,currentTrafficDO,unUpdatedTrafficIds);
        return useTrafficVO;
    }



    /**
     * 将Do类转换成Vo类
     * @param trafficDO
     * @return
     */
    private TrafficVO beanProcess(TrafficDO trafficDO) {
        TrafficVO trafficVO = new TrafficVO();
        BeanUtils.copyProperties(trafficDO,trafficVO);
        //惰性更新，前端显示的问题，根据更新时间进行判断是否需要显示最新的流量包
        String todayStr = TimeUtil.format(new Date(),"yyyy-MM-dd");
        String trafficUpdateStr = TimeUtil.format(trafficDO.getGmtModified(),"yyyy-MM-dd");
        if(!todayStr.equalsIgnoreCase(trafficUpdateStr)){
            trafficVO.setDayUsed(0);
        }
        return trafficVO;
    }

}
