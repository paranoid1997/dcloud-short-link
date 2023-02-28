package net.xdclass.controller.request;

import lombok.Data;

import java.util.Date;

/**
 * @Description
 * @Author:刘森飚
 **/


@Data
public class ShortLinkAddRequest {


    /**
     * 组
     */
    private Long groupId;

    /**
     * 短链标题
     */
    private String title;

    /**
     * 原生url
     */
    private String originalUrl;

    /**
     * 域名id
     */
    private Long domainId;

    /**
     * 域名类型
     */
    private String domainType;

    /**
     * 过期时间
     */
    private Date expired;

}
