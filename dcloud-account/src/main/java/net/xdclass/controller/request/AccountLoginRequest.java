package net.xdclass.controller.request;

import lombok.Data;

/**
 * @Description
 * @Author 刘森飚
 **/

@Data
public class AccountLoginRequest {

    private String phone;

    private String pwd;
}
