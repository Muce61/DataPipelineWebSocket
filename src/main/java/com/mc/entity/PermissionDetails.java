package com.mc.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

/**
 * @author linjunnan
 * @version 1.0.0
 * @project IntelliJ IDEA
 * @description TODO 数据信息实体类，对应数据库表 data_info。
 * @date 2024/7/2 09:35:41
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("permission_details")
public class PermissionDetails {
    /** 权限ID */
    private Integer permissionId;

    /** 权限编号 */
    private String permissionCode;

    /** 权限名称 */
    private String permissionName;

    /** 权限说明 */
    private String permissionDescription;

    /** 权限类型 */
    private String permissionType;

    /** 权限类型说明 */
    private String permissionTypeDescription;

    /** uuid */
    private String uuid;

    /** 状态 */
    private Byte status;

    /** 备注 */
    private String remarks;

    /** 创建时间 */
    private Timestamp createdTime;

    /** 创建者 */
    private String createdBy;

    /** 更新时间 */
    private Timestamp updatedTime;

    /** 更新者 */
    private String updatedBy;

}