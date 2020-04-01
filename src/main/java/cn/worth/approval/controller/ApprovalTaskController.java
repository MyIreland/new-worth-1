package cn.worth.approval.controller;

import cn.worth.approval.domain.ApprovalTask;
import cn.worth.approval.service.IApprovalTaskService;
import cn.worth.approval.vo.ApprovalTaskVO;
import cn.worth.common.annotation.CurrentUser;
import cn.worth.common.domain.R;
import cn.worth.core.controller.BaseController;
import cn.worth.core.domain.LoginUser;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 审批任务 前端控制器
 * </p>
 *
 * @author chenxiaoqing
 * @since 2019-09-17
 */
@RestController
@RequestMapping("/approvalTask")
public class ApprovalTaskController extends BaseController<IApprovalTaskService, ApprovalTask> {

    /**
     * 获取我审批的任务
     * @param entityPage
     * @param loginUser
     * @return
     */
    @PostMapping("/pageMyApprove")
    public R pageMyApprove(Page<ApprovalTaskVO> entityPage, @CurrentUser LoginUser loginUser) {
        Page<ApprovalTaskVO> page = baseService.pageMyApprove(entityPage, loginUser.getId());
        return R.success(page);
    }

    /**
     * 审核任务修改状态
     * @param taskId
     * @param status
     * @param loginUser
     * @return
     */
    @PostMapping("/updateCurrentProcessStatus")
    public R updateCurrentProcessStatus(Long taskId, Integer status, @CurrentUser LoginUser loginUser) {
        return R.success(baseService.updateCurrentProcessStatus(taskId, status, loginUser.getId()));
    }

    /**
     * 撤回审批
     * @param taskId
     * @param loginUser
     * @return
     */
    @PostMapping("recall")
    public R recall(Long taskId, @CurrentUser LoginUser loginUser) {
        return R.success(baseService.recall(taskId, loginUser.getId()));
    }

    /**
     * 通过ID查询
     *
     * @param id ID
     * @return ApprovalTask
     */
    @GetMapping("/{id}")
    public R<ApprovalTaskVO> get(@PathVariable Long id) {
        return new R<>(baseService.getVO(id));
    }

    /**
     * 分页查询信息
     *
     * @return 分页对象
     */
    @PostMapping("/page")
    public R page(Page<ApprovalTaskVO> entityPage, ApprovalTaskVO vo) {
        Page<ApprovalTaskVO> page = baseService.pageVO(entityPage, vo);
        return R.success(page);
    }

    /**
     * 查询当前用户创建的审批
     *
     * @return 分页对象
     */
    @PostMapping("/pageByUser")
    public R pageByUser(Page<ApprovalTaskVO> entityPage, ApprovalTaskVO vo, @CurrentUser LoginUser loginUser) {
        Page<ApprovalTaskVO> page = baseService.pageByUser(entityPage, vo, loginUser.getId());
        return R.success(page);
    }

    /**
     * 添加
     *
     * @param task 实体
     * @return success/false
     */
    @PostMapping("add")
    public R<ApprovalTask> add(Long modelId, ApprovalTask task, @CurrentUser LoginUser loginUser) {
        return new R<>(baseService.add(modelId, task, loginUser));
    }

    /**
     * 删除
     *
     * @param id ID
     * @return success/false
     */
    @DeleteMapping("/{id}")
    public R<Boolean> delete(@PathVariable Long id) {
        return new R<>(baseService.removeById(id));
    }

    /**
     * 编辑
     *
     * @param approvalTask 实体
     * @return success/false
     */
    @PutMapping
    public R<Boolean> edit(@RequestBody ApprovalTask approvalTask) {

        ApprovalTask updateTask = new ApprovalTask();
        updateTask.setId(approvalTask.getId());
        updateTask.setName(approvalTask.getName());
        updateTask.setStatus(approvalTask.getStatus());
        return new R<>(baseService.updateById(updateTask));
    }
}
