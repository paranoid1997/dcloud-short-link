package net.xdclass.controller.request;

import lombok.Data;

/**
 * @author 刘森飚
 * @since 2023-02-20
 */


@Data
public class FrequentSourceRequset {

    /**
     * 短链码
     */
    private String code;

    /**
     * 开始时间
     */
    private String startTime;

    /**
     * 结束时间
     */
    private String endTime;
}
