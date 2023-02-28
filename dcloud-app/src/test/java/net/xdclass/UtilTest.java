package net.xdclass;

import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.UserAgent;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @author 刘森飚
 * @since 2023-02-15
 */

@Slf4j
public class UtilTest {

    @Test
    public void testUserAgentUtil(){
        String userAgentStr = "Mozilla/5.0 (Linux; Android 10; LIO-AN00 Build/HUAWEILIO-AN00; wv)" +
                " AppleWebKit/537.36 (KHTML, like Gecko) " +
                "Version/4.0 Chrome/77.0.3865.120 MQQBrowser/6.2 TBS/045713 Mobile Safari/537.36 MMWEBID/3189" +
                " MicroMessenger/8.0.11.1980(0x28000B51) Process/tools WeChat/arm64 Weixin NetType/WIFI " +
                "Language/zh_CN ABI/arm64";
        UserAgent userAgent = UserAgent.parseUserAgentString(userAgentStr);
        Browser browser = userAgent.getBrowser();
        OperatingSystem operatingSystem = userAgent.getOperatingSystem();
        String browserName = browser.getGroup().getName();
        String os = operatingSystem.getGroup().getName();
        String manufacture = operatingSystem.getManufacturer().getName();
        String deviceType = operatingSystem.getDeviceType().getName();
        System.out.println("browserName="+browserName+",os="+os+",manufacture="+manufacture+",deviceType="+deviceType);
    }
}
