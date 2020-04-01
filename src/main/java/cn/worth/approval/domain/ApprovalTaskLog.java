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
 * 审批任务操作记录表
 * </p>
 *
 * @author chenxiaoqing
 * @since 2019-09-17
 */
@TableName("approval_task_log")
public class ApprovalTaskLog extends Model<ApprovalTaskLog> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 操作类型 1-数据更新
     */
    private Integer type;
    /**
     * 操作任务id
     */
    @TableField("action_task_id")
    private Long actionTaskId;
    /**
     * 操作人
     */
    @TableField("action_user")
    private Long actionUser;
    /**
     * 操作人姓名
     */
    @TableField("action_user_name")
    private String actionUserName;
    /**
     * 操作后状态
     */
    @TableField("action_status")
    private Integer actionStatus;
    /**
     * 操作时间
     */
    @TableField("action_time")
    private Date actionTime;

    public String getActionUserName() {
        return actionUserName;
    }

    public void setActionUserName(String actionUserName) {
        this.actionUserName = actionUserName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Long getActionTaskId() {
        return actionTaskId;
    }

    public void setActionTaskId(Long actionTaskId) {
        this.actionTaskId = actionTaskId;
    }

    public Long getActionUser() {
        return actionUser;
    }

    public void setActionUser(Long actionUser) {
        this.actionUser = actionUser;
    }

    public Integer getActionStatus() {
        return actionStatus;
    }

    public void setActionStatus(Integer actionStatus) {
        this.actionStatus = actionStatus;
    }

    public Date getActionTime() {
        return actionTime;
    }

    public void setActionTime(Date actionTime) {
        this.actionTime = actionTime;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "ApprovalTaskLog{" +
        ", id=" + id +
        ", type=" + type +
        ", actionTaskId=" + actionTaskId +
        ", actionUser=" + actionUser +
        ", actionStatus=" + actionStatus +
        ", actionTime=" + actionTime +
        "}";
    }
}
