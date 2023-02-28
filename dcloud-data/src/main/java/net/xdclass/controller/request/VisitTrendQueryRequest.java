package net.xdclass.controller.request;

import lombok.Data;

/**
 * @author 刘森飚
 * @since 2023-02-20
 */
@Data
public class VisitTrendQueryRequest {

    private String code;
    /**
     * 跨天、当天24小时、分钟级别
     */
    private String type;

    private String startTime;

    private String endTime;

}