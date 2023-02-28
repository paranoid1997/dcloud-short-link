package net.xdclass.controller.request;

import lombok.Data;

/**
 * @author 刘森飚
 * @since 2023-02-19
 */

@Data
public class VisitRecordPageRequest {

    /**
     * 短链码
     */
    private String code;

    /**
     * 一页多少条数据
     */
    private int size;

    /**
     * 第几页
     */
    private int page;
}