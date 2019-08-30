package com.ncut.backmanagement.domain;

import com.baomidou.mybatisplus.enums.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 用户基本信息表
 * </p>
 *
 * @author xiadong
 * @since 2019-08-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("user")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户唯一id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 用户名
     */
    private String name;
    /**
     * 电子邮箱
     */
    private String email;
    /**
     * 电话号码
     */
    private String phoneNumber;
    /**
     * 密码
     */
    private String password;
    /**
     * 用户状态 0-正常 1-封禁
     */
    private Integer status;
    /**
     * 用户账号创建时间
     */
    private Date createTime;
    /**
     * 上次登录时间
     */
    private Date lastLoginTime;
    /**
     * 上次更新记录时间
     */
    private Date lastUpdateTime;
    /**
     * 头像
     */
    private String avatar;


}
