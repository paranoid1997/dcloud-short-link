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
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeviceInfoDO {

    /**
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
}
