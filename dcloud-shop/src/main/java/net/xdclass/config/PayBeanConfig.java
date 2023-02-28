package net.xdclass.config;

import com.wechat.pay.contrib.apache.httpclient.WechatPayHttpClientBuilder;
import com.wechat.pay.contrib.apache.httpclient.auth.PrivateKeySigner;
import com.wechat.pay.contrib.apache.httpclient.auth.ScheduledUpdateCertificatesVerifier;
import com.wechat.pay.contrib.apache.httpclient.auth.WechatPay2Credentials;
import com.wechat.pay.contrib.apache.httpclient.auth.WechatPay2Validator;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.impl.client.CloseableHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.stream.Collectors;

/**
 * @author 刘森飚
 * @since 2023-02-02
 */

@Configuration
@Slf4j
public class PayBeanConfig {

    @Autowired
    private WechatPayConfig payConfig;

    /**
     * 加载秘钥
     *
     * @return
     * @throws IOException
     */

    public PrivateKey getPrivateKey() throws IOException {
        InputStream inputStream = new ClassPathResource(payConfig.getPrivateKeyPath()
                .replace("classpath:", "")).getInputStream();

        String content = new BufferedReader(new InputStreamReader(inputStream))
                .lines().collect(Collectors.joining(System.lineSeparator()));
        try {
            String privateKey = content.replace("-----BEGIN PRIVATE KEY-----", "")
                    .replace("-----END PRIVATE KEY-----", "")
                    .replaceAll("\\s+", "");
            KeyFactory kf = KeyFactory.getInstance("RSA");
            PrivateKey finalPrivateKey = kf.generatePrivate(
                    new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKey)));
            return finalPrivateKey;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("当前Java环境不支持RSA", e);
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException("无效的密钥格式");
        }
    }


    /**
     * 定时获取微信签名验证器，
     * 自动获取微信平台证书（证书里面包括微信平台公钥）
     *
     * @return
     */
    @Bean
    public ScheduledUpdateCertificatesVerifier getCertificatesVerifier() throws IOException {

        // 使用定时更新的签名验证器，不需要传入证书
        ScheduledUpdateCertificatesVerifier verifier = null;
        verifier = new ScheduledUpdateCertificatesVerifier(
                new WechatPay2Credentials(payConfig.getMchId(),
                        new PrivateKeySigner(payConfig.getMchSerialNo(),
                                getPrivateKey())),
                payConfig.getApiV3Key().getBytes(StandardCharsets.UTF_8));

        return verifier;
    }


    /**
     * 获取http请求对象，会自动的处理签名和验签，
     * 并进行证书自动更新
     *
     * @return
     */
    @Bean("wechatPayClient")
    public CloseableHttpClient getWechatPayClient(ScheduledUpdateCertificatesVerifier verifier) throws IOException {
        WechatPayHttpClientBuilder builder = WechatPayHttpClientBuilder.create()
                .withMerchant(payConfig.getMchId(),payConfig.getMchSerialNo() , getPrivateKey())
                .withValidator(new WechatPay2Validator(verifier));

        // 通过WechatPayHttpClientBuilder构造的HttpClient，会自动的处理签名和验签，并进行证书自动更新
        CloseableHttpClient httpClient = builder.build();

        return httpClient;
    }
}