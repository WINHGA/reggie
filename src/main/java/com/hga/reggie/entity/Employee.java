package com.hga.reggie.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 *  员工实体类：
 *
 */
@Data
public class Employee implements Serializable {

    private static final long serialVersionUID = 1L;

    // 将long型转换成 string 型 在前后端数据交换时。
    // @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    private String username;

    private String name;

    private String password;

    private String phone;

    private String sex;

    private String idNumber;      // 身份证号码

    private Integer status;

    @TableField(fill = FieldFill.INSERT)   // 插入时填充字段
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)    // 更新和插入时填充字段
    private LocalDateTime updateTime;

    @TableField(fill = FieldFill.INSERT)     // 插入时填充字段
    private Long createUser;

    @TableField(fill = FieldFill.INSERT_UPDATE)     // 插入时填充字段
    private Long updateUser;

}
