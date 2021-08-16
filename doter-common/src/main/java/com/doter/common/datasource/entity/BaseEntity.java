package com.doter.common.datasource.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
  *@CLassName BaseEntity
  *@Description TDD
  *@Author HTP-韩天鹏
  *@Date 2021/8/10 7:54
  *@Version 1.0
  **/
@Data
@Accessors(chain = true)
public class BaseEntity {

    /**
     * 是否删除：0否，1是
     */
    @TableLogic
    @JsonIgnore
    @TableField(value = "deleted",fill = FieldFill.INSERT)
    private Byte deleted;

    /**
     * 创建时间
     */
    @TableField(value = "create_time",fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time",fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
