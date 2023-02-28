package net.xdclass.controller.request;

import lombok.Data;

/**
 * @author 刘森飚
 * @since 2023-01-30
 */


@Data
public class ProductOrderPageRequest {


    /**
     * 状态
     */
    private String state;

    /**
     * 第几页
     */
    private int page;

    /**
     * 每页多少条
     */
    private int size;

}
