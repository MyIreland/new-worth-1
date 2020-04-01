package cn.worth.approval.service;


import cn.worth.approval.domain.ApprovalModel;
import cn.worth.approval.vo.ApprovalModelVO;
import cn.worth.core.domain.LoginUser;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 审批模型 服务类
 * </p>
 *
 * @author chenxiaoqing
 * @since 2019-09-17
 */
public interface IApprovalModelService extends IService<ApprovalModel> {

    Page<ApprovalModelVO> page(Page<ApprovalModelVO> entityPage, ApprovalModelVO entity);

    ApprovalModelVO get(Long id);

    Boolean add(ApprovalModelVO modelVO, LoginUser loginUser);

    Boolean update(ApprovalModelVO modelVO);

    Boolean updateStatus(ApprovalModel model);

    /**
     * 获取类型使用中的模型
     * @param type
     * @return
     */
    ApprovalModel getInUse(Integer type);
}
