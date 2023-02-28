package net.xdclass.aspect;

import lombok.extern.slf4j.Slf4j;
import net.xdclass.annotation.RepeatSubmit;
import net.xdclass.constant.RedisKey;
import net.xdclass.enums.BizCodeEnum;
import net.xdclass.exception.BizException;
import net.xdclass.interceptor.LoginInterceptor;
import net.xdclass.util.CommonUtil;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * @author 刘森飚
 * descript:定义一个切面类
 * @since 2023-02-01
 */


@Aspect
@Component
@Slf4j
public class RepeatSubmitAspect {


    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private RedissonClient redissonClient;



    /**
     * 定义 @Pointcut注解表达式，
     * 方式一：@annotation：当执行的方法上拥有指定的注解时生效（我们采用这）
     * 方式二：execution：一般用于指定方法的执行
     */
    @Pointcut("@annotation(repeatSubmit)")
    public void pointCutNoRepeatSubmit(RepeatSubmit repeatSubmit) {

    }


    /**
     * 环绕通知, 围绕着方法执行
     *
     * @param joinPoint
     * @throws Throwable
     * @Around 可以用来在调用一个具体方法前和调用后来完成一些具体的任务。
     * 方式一：单用 @Around("execution(* net.xdclass.controller.*.*(..))")可以
     * 方式二：用@Pointcut和@Around联合注解也可以（我们采用这个）
     *
     * 两种方式
     * 方式一：加锁 固定时间内不能重复提交
     * 方式二：先请求获取token，这边再删除token,删除成功则是第一次提交
     */
    @Around("pointCutNoRepeatSubmit(repeatSubmit)")
    public Object around(ProceedingJoinPoint joinPoint, RepeatSubmit repeatSubmit) throws Throwable {
        //拿到前端传来的请求
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes()).getRequest();

        long accountNo = LoginInterceptor.threadLocal.get().getAccountNo();
        //用于记录成功或者失败
        boolean res = false;
        //防重提交类型
        String type = repeatSubmit.limitType().name();
        if(type.equalsIgnoreCase(RepeatSubmit.Type.PARAM.name())){
            //方式一，参数形式防重提交
            //拿到加锁时间
            long lockTime = repeatSubmit.lockTime();
            //拿到ip地址
            String ipAddr = CommonUtil.getIpAddr(request);
            //拿到方法签名
            MethodSignature methodSignature = (MethodSignature)joinPoint.getSignature();
            //拿到方法名
            Method method = methodSignature.getMethod();
            //拿到类名
            String className = method.getDeclaringClass().getName();
            //拿到唯一key
            String key = "order-server:repeat_submit:"
                    + CommonUtil.MD5(String.format("%s-%s-%s-%s",ipAddr,className,method,accountNo));
            //加锁
//            res  = redisTemplate.opsForValue().setIfAbsent(key, "1", lockTime, TimeUnit.SECONDS);
            RLock lock = redissonClient.getLock(key);
            // 尝试加锁，最多等待0秒，上锁以后5秒自动解锁 [lockTime默认为5s, 可以自定义]
            res = lock.tryLock(0,lockTime,TimeUnit.SECONDS);


        }else {
            //方式二，令牌形式防重提交
            String requestToken = request.getHeader("request-token");
            if(StringUtils.isBlank(requestToken)){
                throw new BizException(BizCodeEnum.ORDER_CONFIRM_TOKEN_EQUAL_FAIL);
            }
            String key = String.format(RedisKey.SUBMIT_ORDER_TOKEN_KEY,accountNo,requestToken);
            /**
             * 提交表单的token key
             * 方式一：不用lua脚本获取再判断，之前是因为 key组成是 order:submit:accountNo,
             * value是对应的token，所以需要先获取值，再判断
             * 方式二：可以直接key是 order:submit:accountNo:token,然后直接删除成功则完成
             */
            res = redisTemplate.delete(key);
        }
        if(!res){
            //订单重复提交
            log.error("请求重复提交");
            throw new BizException(BizCodeEnum.ORDER_CONFIRM_REPEAT);
        }
        log.info("环绕通知执行前");
        //订单没有重复提交，继续向下处理
        Object obj = joinPoint.proceed();
        log.info("环绕通知执行后");
        return obj;
    }
}
