package cn.worth.approval.domain;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 审批模型
 * </p>
 *
 * @author chenxiaoqing
 * @since 2019-09-17
 */
@TableName("approval_model")
public class ApprovalModel extends Model<ApprovalModel> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 审批模型名称
     */
    private String name;
    /**
     * 审批模型类型  字典值 approval_type
     */
    private Integer type;
    /**
     * 审批模型状态 0-未启用 1-启用
     */
    private Integer status;
    /**
     * 创建时间
     */
    @TableField("gmt_create")
    private Date gmtCreate;
    /**
     * 创建人
     */
    @TableField("user_create")
    private Long userCreate;
    /**
     * 更新时间
     */
    @TableField("gmt_update")
    private Date gmtUpdate;
    /**
     * 更新人
     */
    @TableField("user_update")
    private Long userUpdate;
    /**
     * 是否删除 0-否 1-是
     */
    @TableField("del_flag")
    private Integer delFlag;

    @TableField("tenant_id")
    private Long tenantId;

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Long getUserCreate() {
        return userCreate;
    }

    public void setUserCreate(Long userCreate) {
        this.userCreate = userCreate;
    }

    public Date getGmtUpdate() {
        return gmtUpdate;
    }

    public void setGmtUpdate(Date gmtUpdate) {
        this.gmtUpdate = gmtUpdate;
    }

    public Long getUserUpdate() {
        return userUpdate;
    }

    public void setUserUpdate(Long userUpdate) {
        this.userUpdate = userUpdate;
    }

    public Integer getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "ApprovalModel{" +
        ", id=" + id +
        ", name=" + name +
        ", type=" + type +
        ", status=" + status +
        ", gmtCreate=" + gmtCreate +
        ", userCreate=" + userCreate +
        ", gmtUpdate=" + gmtUpdate +
        ", userUpdate=" + userUpdate +
        ", delFlag=" + delFlag +
        "}";
    }
}
