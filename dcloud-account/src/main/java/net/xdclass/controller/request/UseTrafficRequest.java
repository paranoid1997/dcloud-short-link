package net.xdclass.controller.request;

import lombok.Data;

/**
 * @author 刘森飚
 * @since 2023-02-07
 */


@Data
public class UseTrafficRequest {

    /**
     * 账号
     */
    private Long accountNo;

    /**
     * 业务id, 短链码
     */
    private String bizId;
}
