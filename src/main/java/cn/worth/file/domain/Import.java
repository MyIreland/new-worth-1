package cn.worth.file.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author chenxiaoqing
 * @since 2019-10-11
 */
@Getter
@Setter
@TableName("sys_import")
public class Import extends Model<Import> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 文件id
     */
    @TableField("file_id")
    private Long fileId;
    /**
     * 文件路径
     */
    @TableField("file_url")
    private String fileUrl;
    /**
     * 文件头行数
     */
    @TableField("header_row")
    private Integer headerRow;
    /**
     * 总行书
     */
    @TableField("total_row")
    private Long totalRow;
    /**
     * 成功导入行数
     */
    @TableField("success_row")
    private Long successRow;
    /**
     * 状态 0-未开始 1-进行中 2-成功 3-失败
     */
    private Integer status;
    /**
     * 导入者
     */
    @TableField("user_create")
    private Long userCreate;
    /**
     * 导入时间
     */
    @TableField("gmt_create")
    private Date gmtCreate;
    /**
     * 修改时间
     */
    @TableField("gmt_modify")
    private Date gmtModify;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "Import{" +
        ", id=" + id +
        ", fileId=" + fileId +
        ", fileUrl=" + fileUrl +
        ", headerRow=" + headerRow +
        ", totalRow=" + totalRow +
        ", successRow=" + successRow +
        ", status=" + status +
        ", userCreate=" + userCreate +
        ", gmtCreate=" + gmtCreate +
        ", gmtModify=" + gmtModify +
        "}";
    }
}
