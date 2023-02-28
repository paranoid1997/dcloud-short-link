package net.xdclass.component;


import lombok.extern.slf4j.Slf4j;
import net.xdclass.config.SmsConfig;
import net.xdclass.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@Slf4j
public class SmsComponent {
    /**
     * 发送地址
     */
    private static final String URL_TEMPLATE =
            "https://jmsms.market.alicloudapi.com/sms/send?mobile=%s&templateId=%s&value=%s";

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private SmsConfig smsConfig;



    /**
     * 发送短信验证码
     * @param to
     * @param templateId
     * @param value
     */
    @Async("threadPoolTaskExecutor")
    public void send(String to,String templateId,String value) {

        long beginTime = CommonUtil.getCurrentTimestamp();

        String url = String.format(URL_TEMPLATE,to,templateId,value);
        HttpHeaders headers = new HttpHeaders();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.set("Authorization","APPCODE " + smsConfig.getAppCode());
        HttpEntity entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

        long endTime = CommonUtil.getCurrentTimestamp();

        log.info("耗时={},url={},body={}",endTime - beginTime,url,response.getBody());
        if (response.getStatusCode().is2xxSuccessful()) {
            log.info("发送短信验证码成功");
        }else {
            log.error("发送短信验证码失败:{}",response.getBody());
        }
    }
}
