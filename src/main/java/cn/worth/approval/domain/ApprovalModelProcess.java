package cn.worth.approval.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.io.Serializable;

/**
 * <p>
 * 审批节点
 * </p>
 *
 * @author chenxiaoqing
 * @since 2019-09-17
 */
@TableName("approval_model_process")
public class ApprovalModelProcess extends Model<ApprovalModelProcess> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 节点名称
     */
    private String name;
    /**
     * 节点排序
     */
    private Integer sort;
    /**
     * 节点审批人
     */
    @TableField("user_id")
    private Long userId;
    /**
     * 节点审批人
     */
    @TableField("user_name")
    private String userName;
    /**
     * 模型id
     */
    @TableField("model_id")
    private Long modelId;
    /**
     * 描述
     */
    private String description;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getModelId() {
        return modelId;
    }

    public void setModelId(Long modelId) {
        this.modelId = modelId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "ApprovalModelProcess{" +
        ", id=" + id +
        ", name=" + name +
        ", sort=" + sort +
        ", userId=" + userId +
        ", modelId=" + modelId +
        ", description=" + description +
        "}";
    }
}
