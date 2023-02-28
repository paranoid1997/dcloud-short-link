package net.xdclass.manager.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import net.xdclass.manager.TrafficManager;
import net.xdclass.mapper.TrafficMapper;
import net.xdclass.model.TrafficDO;
import net.xdclass.util.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * @author 刘森飚
 * @since 2023-02-06
 */


@Component
@Slf4j
public class TrafficManagerImpl implements TrafficManager {


    @Autowired
    private TrafficMapper trafficMapper;


    /**
     * 新增流量包
     * @param trafficDO
     * @return
     */
    @Override
    public int add(TrafficDO trafficDO) {
        return trafficMapper.insert(trafficDO);
    }



    /**
     * 分页查询可用的流量包
     * @param page
     * @param size
     * @param accountNo
     * @return
     */
    @Override
    public IPage<TrafficDO> pageAvailable(int page, int size, Long accountNo) {
        Page<TrafficDO> pageInfo = new Page<>(page, size);
        String today = TimeUtil.format(new Date(), "yyyy-MM-dd");

        Page<TrafficDO> trafficDOPage = trafficMapper.selectPage(pageInfo, new QueryWrapper<TrafficDO>()
                .eq("account_no", accountNo)
                .ge("expired_date", today)
                .orderByDesc("gmt_create"));

        return trafficDOPage;
    }


    /**
     * 查找详情
     * @param trafficId
     * @param accountNo
     * @return
     */
    @Override
    public TrafficDO findByIdAndAccountNo(Long trafficId, Long accountNo) {
        TrafficDO trafficDO = trafficMapper.selectOne(new QueryWrapper<TrafficDO>()
                .eq("account_no", accountNo)
                .eq("id", trafficId));
        return trafficDO;
    }


    /**
     * 删除过期流量包
     * @return
     */
    @Override
    public boolean deleteExpireTraffic() {
        int rows = trafficMapper.delete(new QueryWrapper<TrafficDO>()
                .le("expired_date",new Date()));
        log.info("删除过期流量包行数：rows={}",rows);
        return true;
    }



    /**
     * 查找可用的短链流量包(未过期),包括免费流量包
     * 为了过滤出免费和付费流量包
     *  select * from traffic where account_no =111 and (expired_date >= ? OR out_trade_no=free_init )
     * @param accountNo
     * @return
     */
    @Override
    public List<TrafficDO> selectAvailableTraffics(Long accountNo) {
        String today = TimeUtil.format(new Date(),"yyyy-MM-dd");
        QueryWrapper<TrafficDO> queryWrapper = new QueryWrapper<TrafficDO>();
        queryWrapper.eq("account_no",accountNo);
        //拿到免费流量包和付费流量包
        queryWrapper.and(wrapper->wrapper.ge("expired_date",today)
                .or().eq("out_trade_no","free_init"));
        return trafficMapper.selectList(queryWrapper);
    }



    /**
     * 增加流量包使用次数
     * @param accountNo
     * @param trafficId
     * @param usedTimes
     * @return
     */
    @Override
    public int addDayUsedTimes(Long accountNo, Long trafficId, Integer usedTimes) {

        return trafficMapper.addDayUsedTimes(accountNo,trafficId,usedTimes);
    }



    /**
     * 恢复某个流量包使用次数，回滚流量包
     * @param accountNo
     * @param trafficId
     * @param usedTimes
     * @return
     */
    @Override
    public int releaseUsedTimes(Long accountNo, Long trafficId, Integer usedTimes,String useDateStr) {
        return trafficMapper.releaseUsedTimes(accountNo,trafficId,usedTimes,useDateStr);
    }



    /**
     * 批量更新流量包使用次数为0
     * @param accountNo
     * @param unUpdatedTrafficIds
     */
    @Override
    public int batchUpdateUsedTimes(Long accountNo, List<Long> unUpdatedTrafficIds) {

        int rows = trafficMapper.update(null, new UpdateWrapper<TrafficDO>()
                .eq("account_no", accountNo)
                .in("id", unUpdatedTrafficIds)
                .set("day_used", 0));

        return rows;
    }
}