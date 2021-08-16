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
 * 菜单权限表
 * @TableName dts_menu
 */
@EqualsAndHashCode(callSuper = true)
@TableName(value ="dts_menu")
@Data
@Accessors(chain = true)
public class DtsMenu extends BaseEntity implements Serializable {
    /**
     * 主键
     */
    @TableId
    private Long id;

    /**
     * 父权限
     */
    private Long pid;

    /**
     * 菜单类型，1目录，2页面，3按钮
     */
    private Integer types;

    /**
     * 标题
     */
    private String title;

    /**
     * 菜单图标
     */
    private String icon;

    /**
     * 类型为页面时，代表前端路由地址，类型为按钮时，代表后端接口地址
     */
    private String path;

    /**
     * 权限表达式
     */
    private String authority;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 备注
     */
    private String remark;

    /**
     * 是否外链
     */
    private Byte isIframe;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}