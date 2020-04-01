package cn.worth.approval.mapper;

import cn.worth.approval.domain.ApprovalTaskProcess;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Set;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author chenxiaoqing
 * @since 2019-09-17
 */
public interface ApprovalTaskProcessMapper extends BaseMapper<ApprovalTaskProcess> {

    Set<Long> getMyApproveTaskIds(@Param("status") Integer status, @Param("userId") Long userId);

    /**
     * 获取task 下一个流程节点
     * @param taskId
     * @param sort
     * @return
     */
    ApprovalTaskProcess nextProcess(@Param("taskId") Long taskId, @Param("sort") Integer sort);
}
