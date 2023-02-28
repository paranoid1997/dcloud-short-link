package net.xdclass.controller.request;

import lombok.Data;

/**
 * @Description
 * @Author:刘森飚
 **/

@Data
public class ShortLinkUpdateRequest {


    /**
     * 组
     */
    private Long groupId;

    /**
     * 映射id
     */
    private Long mappingId;

    /**
     * 短链码
     */
    private String code;


    /**
     * 标题
     */
    private String title;

    /**
     * 域名id
     */
    private Long domainId;

    /**
     * 域名类型
     */
    private String domainType;

}
