package net.xdclass.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author 刘森飚
 * @since 2023-02-02
 */

@Data
@Configuration
@ConfigurationProperties(prefix = "pay.wechat")
public class WechatPayConfig {

    /**
     * 商户号
     */
    private String mchId;

    /**
     * 公众号id 需要和商户号绑定
     */
    private String wxPayAppid;
    /**
     * 商户证书序列号,需要和证书对应
     */
    private String mchSerialNo;
    /**
     * API V3密钥
     */
    private String apiV3Key;
    /**
     * 商户私钥路径（微信服务端会根据证书序列号，找到证书获取公钥进行解密数据）
     */
    private String privateKeyPath;
    /**
     * 支付成功页面跳转
     */
    private String successReturnUrl;

    /**
     * 支付成功，回调通知
     */
    private String callbackUrl;
}