package cn.worth.approval.service.impl;

import cn.worth.approval.domain.ApprovalModel;
import cn.worth.approval.domain.ApprovalModelProcess;
import cn.worth.approval.enums.ModelStatusEnum;
import cn.worth.approval.mapper.ApprovalModelMapper;
import cn.worth.approval.service.IApprovalModelProcessService;
import cn.worth.approval.service.IApprovalModelService;
import cn.worth.approval.vo.ApprovalModelVO;
import cn.worth.common.exception.BusinessException;
import cn.worth.common.utils.AssertUtils;
import cn.worth.common.utils.CollectionUtils;
import cn.worth.core.domain.LoginUser;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

/**
 * <p>
 * 审批模型 服务实现类
 * </p>
 *
 * @author chenxiaoqing
 * @since 2019-09-17
 */
@Service
public class ApprovalModelServiceImpl extends ServiceImpl<ApprovalModelMapper, ApprovalModel> implements IApprovalModelService {

    @Autowired
    private IApprovalModelProcessService modelProcessService;

    @Override
    public Page<ApprovalModelVO> page(Page<ApprovalModelVO> entityPage, ApprovalModelVO entity) {
        List<ApprovalModelVO> modelVOS = baseMapper.pageVO(entityPage, entity);
        for (ApprovalModelVO modelVO : modelVOS) {
            Long id = modelVO.getId();
            List<ApprovalModelProcess> modelProcesses = modelProcessService.selectListByModelId(id);
            modelVO.setProcesses(modelProcesses);
        }
        entityPage.setRecords(modelVOS);
        return entityPage;
    }

    @Override
    public ApprovalModelVO get(Long id) {
        AssertUtils.isNull(id, "id is null");
        ApprovalModelVO modelVO = baseMapper.get(id);
        if(null != modelVO){
            List<ApprovalModelProcess> approvalModelProcesses = modelProcessService.selectListByModelId(id);
            modelVO.setProcesses(approvalModelProcesses);
        }
        return modelVO;
    }

    @Override
    @Transactional
    public Boolean add(ApprovalModelVO modelVO, LoginUser loginUser) {
        List<ApprovalModelProcess> processes = modelVO.getProcesses();
        modelVO.setUserCreate(loginUser.getId());
        modelVO.setTenantId(loginUser.getTenantId());
        boolean result = save(modelVO);
        if(result && CollectionUtils.isNotEmpty(processes)){
            Long modelId = modelVO.getId();
            insertModelProcessBatch(modelId, processes);
        }
        return true;
    }

    private void insertModelProcessBatch(Long modelId, List<ApprovalModelProcess> processes) {
        int sort = 1;
        for (ApprovalModelProcess process : processes) {
            process.setModelId(modelId);
            process.setSort(sort);
            sort++;
        }
        modelProcessService.saveBatch(processes);
    }

    @Override
    @Transactional
    public Boolean update(ApprovalModelVO modelVO) {
        Long modelId = modelVO.getId();
        if(null == modelId){
            throw new BusinessException("modelId is null");
        }
        boolean updateResult = updateById(modelVO);
        Boolean delResult = modelProcessService.deleteByModelId(modelId);
        List<ApprovalModelProcess> processes = modelVO.getProcesses();
        if(updateResult && delResult && CollectionUtils.isNotEmpty(processes)){
            insertModelProcessBatch(modelId, processes);
        }
        return true;
    }

    @Override
    @Transactional
    public Boolean updateStatus(ApprovalModel model) {

        verifyParam(model);
        Integer type = model.getType();
        Integer status = model.getStatus();
        // 一种类型模型只能有一个启用状态
        if(ModelStatusEnum.INUSE.getCode() == status){
            ApprovalModel exitsOne = getInUse(type);
            if(null != exitsOne){
                throw new BusinessException("one approval should be opened by one type");
            }
        }
        ApprovalModel modelForUpdate = new ApprovalModel();
        modelForUpdate.setId(model.getId());
        modelForUpdate.setStatus(status);
        return updateById(modelForUpdate);
    }

    @Override
    public ApprovalModel getInUse(Integer type) {
        QueryWrapper<ApprovalModel> wrapper = new QueryWrapper<>();
        wrapper.eq("status", ModelStatusEnum.INUSE.getCode());
        wrapper.eq("type", type);

        return getOne(wrapper);
    }

    private void verifyParam(ApprovalModel model) {
        Long id = model.getId();
        if(null == id){
            throw new BusinessException("id is null");
        }
        Integer status = model.getStatus();
        if(null == status){
            throw new BusinessException("status is null");
        }
        Integer type = model.getType();
        if(null == type){
            throw new BusinessException("type is null");
        }
    }

}
