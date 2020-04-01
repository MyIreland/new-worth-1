package cn.worth.approval.controller;

import cn.worth.approval.domain.ApprovalTaskProcess;
import cn.worth.approval.service.IApprovalTaskProcessService;
import cn.worth.common.annotation.CurrentUser;
import cn.worth.common.domain.R;
import cn.worth.core.controller.BaseController;
import cn.worth.core.domain.LoginUser;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author chenxiaoqing
 * @since 2019-09-17
 */
@RestController
@RequestMapping("/approvalTaskProcess")
public class ApprovalTaskProcessController extends BaseController<IApprovalTaskProcessService, ApprovalTaskProcess> {

    @PostMapping("updateStatus")
    public R<Boolean> updateStatus(Long taskProcessId, Integer status, @CurrentUser LoginUser loginUser){
        return new R<>(baseService.updateStatus(taskProcessId, status, loginUser.getId()));
    }
}
