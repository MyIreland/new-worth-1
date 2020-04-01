package cn.worth.approval.mapper;

import cn.worth.approval.domain.ApprovalModel;
import cn.worth.approval.vo.ApprovalModelVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 审批模型 Mapper 接口
 * </p>
 *
 * @author chenxiaoqing
 * @since 2019-09-17
 */
public interface ApprovalModelMapper extends BaseMapper<ApprovalModel> {

    ApprovalModelVO get(@Param("id") Long id);

    List<ApprovalModelVO> pageVO(Page<ApprovalModelVO> entityPage, ApprovalModelVO entity);
}
