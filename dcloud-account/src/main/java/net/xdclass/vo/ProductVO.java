package net.xdclass.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * @author 刘森飚
 * @since 2023-02-06
 */

@Data
@EqualsAndHashCode(callSuper = false)
public class ProductVO {


    private Long id;

    /**
     * 商品标题
     */
    private String title;

    /**
     * 详情
     */
    private String detail;

    /**
     * 图片
     */
    private String img;

    /**
     * 产品层级：FIRST青铜、SECOND黄金、THIRD钻石
     */
    private String level;

    /**
     * 原价
     */
    private BigDecimal oldAmount;

    /**
     * 现价
     */
    private BigDecimal amount;

    /**
     * 工具类型 short_link、qrcode
     */
    private String pluginType;

    /**
     * 日次数：短链类型
     */
    private Integer dayTimes;

    /**
     * 总次数：活码才有
     */
    private Integer totalTimes;

    /**
     * 有效天数
     */
    private Integer validDay;
}