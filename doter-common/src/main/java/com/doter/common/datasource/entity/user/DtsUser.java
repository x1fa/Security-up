package com.doter.common.datasource.entity.user;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.doter.common.datasource.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 
 * @TableName dts_user
 */
@EqualsAndHashCode(callSuper = true)
@TableName(value ="dts_user")
@Data
@Accessors(chain = true)
public class DtsUser extends BaseEntity implements Serializable {
    /**
     * 主键
     */
    @TableId
    private Long id;

    /**
     * 组织ID
     */
    private Long organizeId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 密码
     */
    private String passWord;

    /**
     * 姓名
     */
    private String realName;

    /**
     * 头像地址
     */
    private String avatarUrl;

    /**
     * 性别
     */
    private Integer gender;

    /**
     * 手机号码
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 状态：1禁用 2启用
     */
    private Byte status;


    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}