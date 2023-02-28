package net.xdclass.service;

import net.xdclass.enums.SendCodeEnum;
import net.xdclass.util.JsonData;

public interface NotifyService {


    /**
     * 发送短信验证码
     * @param sendCodeEnum
     * @param to
     * @return
     */
    JsonData sendCode(SendCodeEnum sendCodeEnum, String to);


    /**
     * 校验验证码
     * @param sendCodeEnum
     * @param to
     * @param code
     * @return
     */
    boolean checkCode(SendCodeEnum sendCodeEnum, String to,String code);
}
