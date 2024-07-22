package com.mc.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @Description
 * @Author muse
 * @Date 2024/07/16
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("company_info")
public class SecurityCompany {
    @TableId(value = "company_id" ,type = IdType.AUTO)
    private Long id;

    @TableField("company_code")
    private String company_code;

    @TableField("company_name")
    private String company_name;

    @TableField("uuid")
    private String uuid;

    @TableField("status")
    private Integer status;

    @TableField("created_time")
    private LocalDateTime created_time;

    @TableField("created_by")
    private String created_by;

    @TableField("updated_time")
    private LocalDateTime updated_time;

    @TableField("updated_by")
    private String updated_by;

}
