package cn.worth.approval.service;


import cn.worth.approval.domain.ApprovalTaskProcess;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Set;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author chenxiaoqing
 * @since 2019-09-17
 */
public interface IApprovalTaskProcessService extends IService<ApprovalTaskProcess> {

    List<ApprovalTaskProcess> getByTaskId(Long id);

    Boolean updateStatus(Long taskProcessId, Integer status, Long id);

    Set<Long> getMyApproveTaskIds(Integer status, Long userId);
}
