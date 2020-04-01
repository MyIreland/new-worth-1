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
 * 审批任务
 * </p>
 *
 * @author chenxiaoqing
 * @since 2019-09-17
 */
@TableName("approval_task")
public class ApprovalTask extends Model<ApprovalTask> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 审批任务名称
     */
    private String name;
    /**
     * 审批任务类型  字典值 approval_type
     */
    private Integer type;
    /**
     * 审批任务状态 0-进行中 1-审批通过 2-审批拒绝 3-审批取消
     */
    private Integer status;
    /**
     * 当前任务节点
     */
    @TableField("current_process")
    private Long currentProcess;
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
     * 创建人
     */
    @TableField("user_create_name")
    private String userCreateName;

    @TableField("tenant_id")
    private Long tenantId;

    public String getUserCreateName() {
        return userCreateName;
    }

    public void setUserCreateName(String userCreateName) {
        this.userCreateName = userCreateName;
    }

    public Long getCurrentProcess() {
        return currentProcess;
    }

    public void setCurrentProcess(Long currentProcess) {
        this.currentProcess = currentProcess;
    }

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

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "ApprovalTask{" +
        ", id=" + id +
        ", name=" + name +
        ", type=" + type +
        ", status=" + status +
        ", gmtCreate=" + gmtCreate +
        ", userCreate=" + userCreate +
        "}";
    }
}
