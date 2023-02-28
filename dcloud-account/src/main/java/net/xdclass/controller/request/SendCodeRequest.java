package net.xdclass.controller.request;

import lombok.Data;

/**
 * @Author 刘森飚
 **/


@Data
public class SendCodeRequest {

    private String captcha;

    private String to;
}
