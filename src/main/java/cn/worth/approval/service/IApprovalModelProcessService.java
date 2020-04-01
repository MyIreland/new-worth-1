package cn.worth.approval.service;

import cn.worth.approval.domain.ApprovalModelProcess;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 审批节点 服务类
 * </p>
 *
 * @author chenxiaoqing
 * @since 2019-09-17
 */
public interface IApprovalModelProcessService extends IService<ApprovalModelProcess> {

    List<ApprovalModelProcess> selectListByModelId(Long modelId);

    Boolean deleteByModelId(Long id);
}
