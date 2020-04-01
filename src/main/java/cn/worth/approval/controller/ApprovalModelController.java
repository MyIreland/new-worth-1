package cn.worth.approval.controller;

import cn.worth.approval.domain.ApprovalModel;
import cn.worth.approval.service.IApprovalModelService;
import cn.worth.approval.vo.ApprovalModelVO;
import cn.worth.common.annotation.CurrentUser;
import cn.worth.common.constant.CommonConstant;
import cn.worth.common.domain.R;
import cn.worth.core.controller.BaseController;
import cn.worth.core.domain.LoginUser;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * <p>
 * 审批模型 前端控制器
 * </p>
 *
 * @author chenxiaoqing
 * @since 2019-09-17
 */
@RestController
@RequestMapping("/approvalModel")
public class ApprovalModelController extends BaseController<IApprovalModelService, ApprovalModel> {

    /**
     * 通过ID查询
     *
     * @param id ID
     * @return ApprovalModel
     */
    @GetMapping("/{id}")
    public R<ApprovalModelVO> get(@PathVariable Long id) {
        ApprovalModelVO model = baseService.get(id);
        return new R<>(model);
    }


    /**
     * 分页查询信息
     *
     * @return 分页对象
     */
    @PostMapping("/page")
    public R page(Page<ApprovalModelVO> entityPage, ApprovalModelVO vo) {
        Page<ApprovalModelVO> page = baseService.page(entityPage, vo);
        return R.success(page);
    }

    /**
     * 添加
     *
     * @param modelVO 实体
     * @return success/false
     */
    @PostMapping
    public R<Boolean> add(@RequestBody ApprovalModelVO modelVO, @CurrentUser LoginUser loginUser) {
        return new R<>(baseService.add(modelVO, loginUser));
    }

    /**
     * 更改审批状态
     *
     * @param model
     * @return success/false
     */
    @PostMapping("updateStatus")
    public R<Boolean> updateStatus(@RequestBody ApprovalModel model) {
        return new R<>(baseService.updateStatus(model));
    }

    /**
     * 删除
     *
     * @param id ID
     * @return success/false
     */
    @DeleteMapping("/{id}")
    public R<Boolean> delete(@PathVariable Long id) {
        ApprovalModel approvalModel = new ApprovalModel();
        approvalModel.setId(id);
        approvalModel.setDelFlag(CommonConstant.STATUS_DEL);
        approvalModel.setGmtUpdate(new Date());
        return new R<>(baseService.updateById(approvalModel));
    }

    /**
     * 编辑
     *
     * @param modelVO 实体
     * @return success/false
     */
    @PutMapping
    public R<Boolean> edit(@RequestBody ApprovalModelVO modelVO) {
        return new R<>(baseService.update(modelVO));
    }
}
