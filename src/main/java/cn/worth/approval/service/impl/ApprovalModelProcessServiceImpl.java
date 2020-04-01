package cn.worth.approval.service.impl;

import cn.worth.approval.domain.ApprovalModelProcess;
import cn.worth.approval.mapper.ApprovalModelProcessMapper;
import cn.worth.approval.service.IApprovalModelProcessService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 审批节点 服务实现类
 * </p>
 *
 * @author chenxiaoqing
 * @since 2019-09-17
 */
@Service
public class ApprovalModelProcessServiceImpl extends ServiceImpl<ApprovalModelProcessMapper, ApprovalModelProcess> implements IApprovalModelProcessService {

    @Override
    public List<ApprovalModelProcess> selectListByModelId(Long modelId) {
        QueryWrapper<ApprovalModelProcess> modelWrapper = new QueryWrapper<>();
        modelWrapper.eq("model_id", modelId);
        return list(modelWrapper);
    }

    @Override
    public Boolean deleteByModelId(Long id) {
        QueryWrapper<ApprovalModelProcess> modelWrapper = new QueryWrapper<>();
        modelWrapper.eq("model_id", id);
        return remove(modelWrapper);
    }
}
