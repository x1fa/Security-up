package com.doter.common.datasource.entity.user;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.doter.common.datasource.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

/**
  *@CLassName DtaUser
  *@Description 会员用户表
  *@Author HTP-韩天鹏
  *@Date 2021/8/10 8:05
  *@Version 1.0
  **/
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(value ="dta_user")
public class DtaUser extends BaseEntity implements Serializable {
    /**
     * 用户id
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 用户账户
     */
    @TableField(value = "user_name")
    private String userName;

    /**
     * 用户密码
     */
    @TableField(value = "pass_word")
    private String passWord;

    /**
     * 手机号码
     */
    @TableField(value = "phone")
    private String phone;

    /**
     * 微信开放id
     */
    @TableField(value = "wx_unionid")
    private String wxUnionid;

    /**
     * 微信小程序Id
     */
    @TableField(value = "wx_openid")
    private String wxOpenid;

    /**
     * 邮箱
     */
    @TableField(value = "email")
    private String email;

    /**
     * 用户头像
     */
    @TableField(value = "avatar_url")
    private String avatarUrl;

    /**
     * 用户昵称
     */
    @TableField(value = "nick_name")
    private String nickName;

    /**
     * 用户钱包
     */
    @TableField(value = "wallet")
    private BigDecimal wallet;

    /**
     * 支付密码
     */
    @TableField(value = "pay_code")
    private String payCode;

    /**
     * 角色
     */
    @TableField(value = "roles")
    private String roles;

    /**
     * 状态：0禁用 1启用
     */
    @TableField(value = "status")
    private Byte status;


    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}