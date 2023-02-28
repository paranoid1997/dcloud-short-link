package net.xdclass.constant;


/**
 * @author 刘森飚
 * @since 2023-01-30
 */

public class RedisKey {
    /**
     * 验证码缓存key，第一个是类型,第二个是唯一标识比如手机号或者邮箱
     */
    public static final String CHECK_CODE_KEY = "code:%s:%s";


    /**
     * 提交订单令牌的缓存key,第一个是账号，第二个是token
     */
    public static final String SUBMIT_ORDER_TOKEN_KEY = "order:submit:%s:%s";


    /**
     * 1天的可用的总流量包
     */
    public static final String DAY_TOTAL_TRAFFIC = "lock:traffic:day_total:%s";
}
