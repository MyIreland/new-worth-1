package cn.worth.approval.mapper;

import cn.worth.approval.domain.ApprovalTask;
import cn.worth.approval.vo.ApprovalTaskVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 审批任务 Mapper 接口
 * </p>
 *
 * @author chenxiaoqing
 * @since 2019-09-17
 */
public interface ApprovalTaskMapper extends BaseMapper<ApprovalTask> {

    ApprovalTaskVO getVO(@Param("id") Long id);

    List<ApprovalTaskVO> pageVO(Page<ApprovalTaskVO> entityPage, ApprovalTaskVO vo);

    List<ApprovalTaskVO> pageByUser(Page<ApprovalTaskVO> entityPage, @Param("param") ApprovalTaskVO vo, @Param("userId") Long userId);

    List<ApprovalTaskVO> getByIds(Page<ApprovalTaskVO> entityPage, @Param("taskIds") Set<Long> taskIds);

    List<ApprovalTaskVO> pageMyApprove(Page<ApprovalTaskVO> entityPage, @Param("userId") Long userId);
}
