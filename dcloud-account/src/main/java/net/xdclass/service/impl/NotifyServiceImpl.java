package net.xdclass.service.impl;

import lombok.extern.slf4j.Slf4j;
import net.xdclass.component.SmsComponent;
import net.xdclass.config.SmsConfig;
import net.xdclass.constant.RedisKey;
import net.xdclass.enums.BizCodeEnum;
import net.xdclass.enums.SendCodeEnum;
import net.xdclass.service.NotifyService;
import net.xdclass.util.CheckUtil;
import net.xdclass.util.CommonUtil;
import net.xdclass.util.JsonData;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class NotifyServiceImpl implements NotifyService {

    /**
     * 10分钟有效
     */
    private static final int CODE_EXPIRED = 60*1000*10;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private SmsComponent smsComponent;

    @Autowired
    private SmsConfig smsConfig;

    @Autowired
    private StringRedisTemplate redisTemplate;


    @Override
    public JsonData sendCode(SendCodeEnum sendCodeEnum, String to) {
        //code:USER_REGISTER:15861364032
        String cacheKey = String.format(RedisKey.CHECK_CODE_KEY, sendCodeEnum.name(), to);
        String cacheValue = redisTemplate.opsForValue().get(cacheKey);

        //如果不为空，再判断是否60s内重复发送
        //564621_1673704305875
        if (StringUtils.isNotBlank(cacheValue)) {
            long ttl = Long.parseLong(cacheValue.split("_")[1]);
            //当前时间戳-验证码发送的时间戳，如果小于60秒，则不给重复发送
            long leftTime = CommonUtil.getCurrentTimestamp() - ttl;
            if (leftTime < (1000 * 60)) {
                log.info("重复发送短信验证码，时间间隔:{}秒",leftTime);
                return JsonData.buildResult(BizCodeEnum.CODE_LIMITED);
            }
        }

        String code = CommonUtil.getRandomCode(6);
//        String code = "刘森飚接单工作室";
        //生成拼接好的验证码
        String value = code + "_" + CommonUtil.getCurrentTimestamp();
        redisTemplate.opsForValue().set(cacheKey,value,CODE_EXPIRED, TimeUnit.MILLISECONDS);

        if (CheckUtil.isEmail(to)) {
            //发送邮箱验证码 TODO
        }else if(CheckUtil.isPhone(to)) {
            //发送手机验证码
            smsComponent.send(to,smsConfig.getTemplateId(),code);
        }
        return JsonData.buildSuccess();
    }


    /**
     * 验证码校验逻辑
     * @param sendCodeEnum
     * @param to
     * @param code
     * @return
     */
    @Override
    public boolean checkCode(SendCodeEnum sendCodeEnum, String to, String code) {
        String cacheKey = String.format(RedisKey.CHECK_CODE_KEY, sendCodeEnum.name(), to);
        String cacheValue = redisTemplate.opsForValue().get(cacheKey);

        if (StringUtils.isNotBlank(cacheValue)) {
            String cacheCode = cacheValue.split("_")[0];
            if (cacheCode.equalsIgnoreCase(code)) {
                //删除验证码
                redisTemplate.delete(code);
                return true;
            }
        }
        return false;
    }
}
