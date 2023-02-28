package net.xdclass.controller;

import com.google.code.kaptcha.Producer;
import lombok.extern.slf4j.Slf4j;
import net.xdclass.controller.request.SendCodeRequest;
import net.xdclass.enums.BizCodeEnum;
import net.xdclass.enums.SendCodeEnum;
import net.xdclass.service.NotifyService;
import net.xdclass.util.CommonUtil;
import net.xdclass.util.JsonData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @Description
 * @Author 刘森飚
 **/

@RestController
@RequestMapping("/api/notify/v1")
@Slf4j
public class NotifyController {


    @Autowired
    private Producer captchaProducer;


    @Autowired
    private NotifyService notifyService;

    @Autowired
    private StringRedisTemplate redisTemplate;


    /**
     * 验证码过期时间
     */
    private static final long CAPTCHA_CODE_EXPIRED = 1000 * 10 *  60;

    /**
     * 生成验证码
     * @param request
     * @param response
     */
    @GetMapping("captcha")
    public void getCaptcha(HttpServletRequest request, HttpServletResponse response){


        String captchaText = captchaProducer.createText();
        log.info("验证码内容:{}",captchaText);

        //存储redis,配置过期时间
        redisTemplate.opsForValue().set(getCaptchaKey(request),captchaText,CAPTCHA_CODE_EXPIRED,TimeUnit.MILLISECONDS);


        BufferedImage bufferedImage = captchaProducer.createImage(captchaText);

        try (ServletOutputStream outputStream = response.getOutputStream()){

            ImageIO.write(bufferedImage,"jpg",outputStream);
            outputStream.flush();

        } catch (IOException e) {
            log.error("获取流出错:{}",e.getMessage());
        }

    }



    private String getCaptchaKey(HttpServletRequest request){

        String ip = CommonUtil.getIpAddr(request);
        String userAgent = request.getHeader("User-Agent");
        String key = "account-service:captcha:"+CommonUtil.MD5(ip+userAgent);
        log.info("验证码key:{}",key);
        return key;
    }




    /**
     * 发送验证码接口
     * @return
     */
    @PostMapping("send_code")
    public JsonData sendCode(@RequestBody SendCodeRequest sendCodeRequest,HttpServletRequest request){
        String key = getCaptchaKey(request);
        String cacheCaptcha = redisTemplate.opsForValue().get(key);

        String captcha = sendCodeRequest.getCaptcha();
        if (captcha != null && cacheCaptcha != null && cacheCaptcha.equalsIgnoreCase(captcha)) {
            //成功
            redisTemplate.delete(key);
            JsonData jsonData = notifyService.sendCode(SendCodeEnum.USER_REGISTER,sendCodeRequest.getTo());
            return jsonData;

        }else {
        return JsonData.buildResult(BizCodeEnum.CODE_CAPTCHA_ERROR);
        }
    }

}
