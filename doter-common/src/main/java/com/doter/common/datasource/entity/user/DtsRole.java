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
 * 角色表
 * @TableName dts_role
 */
@EqualsAndHashCode(callSuper = true)
@TableName(value ="dts_role")
@Data
@Accessors(chain = true)
public class DtsRole extends BaseEntity implements Serializable {
    /**
     * 主键
     */
    @TableId
    private Long id;

    /**
     * 中文名
     */
    private String title;

    /**
     * 级别
     */
    private Integer roleLevel;

    /**
     * 角色权限
     */
    private String roleScope;

    /**
     * 数据权限
     */
    private String dataScope;

    /**
     * 描述
     */
    private String remark;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}