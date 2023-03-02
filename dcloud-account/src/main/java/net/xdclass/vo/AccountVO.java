package net.xdclass.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * @author 刘森飚
 * @since 2023-03-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class AccountVO {



    private Long accountNo;

    /**
     * 头像
     */
    private String headImg;

    /**
     * 手机号
     */
    private String phone;



    /**
     * 邮箱
     */
    private String mail;

    /**
     * 用户名
     */
    private String username;

    /**
     * 认证级别，DEFAULT，REALNAME，ENTERPRISE，访问次数不一样
     */
    private String auth;

    @JsonProperty("create_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date gmtCreate;



}
