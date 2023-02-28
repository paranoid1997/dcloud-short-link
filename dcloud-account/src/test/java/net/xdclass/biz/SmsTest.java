package net.xdclass.biz;


import lombok.extern.slf4j.Slf4j;
import net.xdclass.AccountApplication;
import net.xdclass.component.SmsComponent;
import net.xdclass.config.SmsConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AccountApplication.class)
@Slf4j
public class SmsTest {

    @Autowired
    private SmsComponent smsComponent;

    @Autowired
    private SmsConfig smsConfig;

    @Test
    public void testSendSms() {
//        for (int i = 0; i < 3; i++) {
        smsComponent.send("15152624496",smsConfig.getTemplateId(),"111111");
        smsComponent.send("13852866477",smsConfig.getTemplateId(),"222222");
        smsComponent.send("15861364032",smsConfig.getTemplateId(),"333333");
        smsComponent.send("13618051265",smsConfig.getTemplateId(),"444444");
        smsComponent.send("18702847624",smsConfig.getTemplateId(),"666666");
//        }

    }
}
