package cn.worth.approval.service;


import cn.worth.approval.domain.ApprovalTask;
import cn.worth.approval.vo.ApprovalTaskVO;
import cn.worth.core.domain.LoginUser;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 审批任务 服务类
 * </p>
 *
 * @author chenxiaoqing
 * @since 2019-09-17
 */
public interface IApprovalTaskService extends IService<ApprovalTask> {

    ApprovalTaskVO getVO(Long id);

    Page<ApprovalTaskVO> pageVO(Page<ApprovalTaskVO> entityPage, ApprovalTaskVO vo);

    ApprovalTask add(Long modelId, ApprovalTask task, LoginUser loginUser);

    Page<ApprovalTaskVO> pageByUser(Page<ApprovalTaskVO> entityPage, ApprovalTaskVO vo, Long id);

    Page<ApprovalTaskVO> pageMyApprove(Page<ApprovalTaskVO> entityPage, Long id);

    Boolean recall(Long taskId, Long userId);

    /**
     * 审批任务当前节点
     * @param taskId
     * @param status
     * @param id
     * @return
     */
    Boolean updateCurrentProcessStatus(Long taskId, Integer status, Long id);
}
