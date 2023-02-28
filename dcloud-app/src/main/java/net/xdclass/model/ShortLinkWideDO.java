package net.xdclass.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 刘森飚
 * @since 2023-02-15
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShortLinkWideDO {

    //======短链业务本身信息==========

    /**
     * 短链码
     */
    private String code;


    /**
     * 账号
     */
    private Long accountNo;


    /**
     * 访问时间
     */
    private Long visitTime;


    /**
     * 站点来源，只记录域名
     */
    private String referer;


    /**
     * 1是新访客，0是旧访客
     */
    private Integer isNew;


    //===============设备相关字段=============

    /**
     *
     * 浏览器名称
     */
    private String browserName;

    /**
     * 系统
     */
    private String os;

    /**
     * 系统版本
     */
    private String osVersion;


    /**
     * 设备类型
     */
    private String deviceType;

    /**
     * 厂商
     */
    private String deviceManufacturer;


    /**
     * 终端用户唯一标识
     */
    private String udid;


    //====地理位置信息=======

    /**
     * 国家
     */
    private String country;

    /**
     * 省份
     */
    private String province;

    /**
     * 城市
     */
    private String city;

    /**
     * 运营商
     */
    private String isp;

    /**
     * 访问来源ip
     */
    private String ip;
}
