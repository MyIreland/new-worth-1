package cn.worth.approval.service.impl;

import cn.worth.common.exception.BusinessException;
import cn.worth.common.utils.CollectionUtils;
import cn.worth.common.utils.StringUtils;
import cn.worth.approval.domain.ApprovalModelProcess;
import cn.worth.approval.domain.ApprovalTask;
import cn.worth.approval.domain.ApprovalTaskProcess;
import cn.worth.approval.enums.ApprovalErrorEnum;
import cn.worth.approval.enums.TaskProcessStatusEnum;
import cn.worth.approval.enums.TaskStatusEnum;
import cn.worth.approval.mapper.ApprovalTaskMapper;
import cn.worth.approval.service.IApprovalModelService;
import cn.worth.approval.service.IApprovalTaskProcessService;
import cn.worth.approval.service.IApprovalTaskService;
import cn.worth.approval.vo.ApprovalModelVO;
import cn.worth.approval.vo.ApprovalTaskVO;
import cn.worth.core.domain.LoginUser;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 审批任务 服务实现类
 * </p>
 *
 * @author chenxiaoqing
 * @since 2019-09-17
 */
@Service
public class ApprovalTaskServiceImpl extends ServiceImpl<ApprovalTaskMapper, ApprovalTask> implements IApprovalTaskService {

    @Autowired
    private IApprovalTaskProcessService taskProcessService;

    @Autowired
    private IApprovalModelService modelService;

    @Override
    public ApprovalTaskVO getVO(Long id) {
        ApprovalTaskVO taskVO = baseMapper.getVO(id);
        List<ApprovalTaskProcess> processes = taskProcessService.getByTaskId(id);
        taskVO.setProcesses(processes);
        return taskVO;
    }

    @Override
    public Page<ApprovalTaskVO> pageVO(Page<ApprovalTaskVO> entityPage, ApprovalTaskVO vo) {
        List<ApprovalTaskVO> taskVOS = baseMapper.pageVO(entityPage, vo);
        for (ApprovalTaskVO taskVO : taskVOS) {
            Long id = taskVO.getId();
            List<ApprovalTaskProcess> modelProcesses = taskProcessService.getByTaskId(id);
            taskVO.setProcesses(modelProcesses);
        }
        entityPage.setRecords(taskVOS);
        return entityPage;
    }

    @Override
    @Transactional
    public ApprovalTask add(Long modelId, ApprovalTask taskVO, LoginUser loginUser) {
        ApprovalModelVO approvalModelVO = modelService.get(modelId);
        List<ApprovalModelProcess> modelProcesses = approvalModelVO.getProcesses();
        ApprovalTask task = genApprovalTask(approvalModelVO, taskVO, loginUser);

        boolean insertResult = save(task);
        if(insertResult && CollectionUtils.isNotEmpty(modelProcesses)){
            List<ApprovalTaskProcess> taskProcesses = new ArrayList<>();
            Long taskId = task.getId();
            Date currentDate = new Date();
            for (ApprovalModelProcess modelProcess : modelProcesses) {
                ApprovalTaskProcess taskProcess = new ApprovalTaskProcess();
                taskProcess.setName(modelProcess.getName());
                taskProcess.setTaskId(taskId);
                taskProcess.setDescription(modelProcess.getDescription());
                taskProcess.setSort(modelProcess.getSort());
                taskProcess.setUserId(modelProcess.getUserId());
                taskProcess.setUserName(modelProcess.getUserName());
                taskProcess.setStatus(TaskProcessStatusEnum.WAIT.getCode());
                taskProcess.setGmtCreate(currentDate);
                taskProcesses.add(taskProcess);
            }
            taskProcessService.saveBatch(taskProcesses);
            Long firstProcess = taskProcesses.get(0).getId();
            if(null != firstProcess){
                ApprovalTask taskForUpdate = new ApprovalTask();
                taskForUpdate.setId(taskId);
                taskForUpdate.setCurrentProcess(firstProcess);
                updateById(taskForUpdate);
            }
        }
        return task;
    }

    @Override
    public Page<ApprovalTaskVO> pageByUser(Page<ApprovalTaskVO> entityPage, ApprovalTaskVO vo, Long userId) {
        List<ApprovalTaskVO> taskVOS = baseMapper.pageByUser(entityPage, vo, userId);
        for (ApprovalTaskVO taskVO : taskVOS) {
            Long id = taskVO.getId();
            List<ApprovalTaskProcess> modelProcesses = taskProcessService.getByTaskId(id);
            taskVO.setProcesses(modelProcesses);
        }
        entityPage.setRecords(taskVOS);
        return entityPage;
    }

    @Override
    public Page<ApprovalTaskVO> pageMyApprove(Page<ApprovalTaskVO> entityPage, Long userId) {

        List<ApprovalTaskVO> taskVOS = baseMapper.pageMyApprove(entityPage, userId);
        if(CollectionUtils.isNotEmpty(taskVOS)){
            for (ApprovalTaskVO taskVO : taskVOS) {
                Long id = taskVO.getId();
                List<ApprovalTaskProcess> modelProcesses = taskProcessService.getByTaskId(id);
                taskVO.setProcesses(modelProcesses);
            }
        }
        entityPage.setRecords(taskVOS);
        return entityPage;
    }

    @Override
    public Boolean recall(Long taskId, Long userId) {
        ApprovalTask task = baseMapper.selectById(taskId);
        if(null == task){
            ApprovalErrorEnum error = ApprovalErrorEnum.TASK_NOT_FOUND;
            throw new BusinessException(error.getCode(), error.getDesc());
        }
        Integer status = task.getStatus();
        if(TaskStatusEnum.RUNNING.getCode() != status){
            ApprovalErrorEnum error = ApprovalErrorEnum.TASK_STATUS_ERROR;
            throw new BusinessException(error.getCode(), error.getDesc());
        }
        Long userCreate = task.getUserCreate();
        if(!userCreate.equals(userId)){
            ApprovalErrorEnum error = ApprovalErrorEnum.TASK_OPTION_USER_ERROR;
            throw new BusinessException(error.getCode(), error.getDesc());
        }

        ApprovalTask taskForUpdate = new ApprovalTask();
        taskForUpdate.setId(taskId);
        taskForUpdate.setStatus(TaskStatusEnum.RECALL.getCode());
        return updateById(taskForUpdate);
    }

    @Override
    public Boolean updateCurrentProcessStatus(Long taskId, Integer status, Long userId) {
        ApprovalTask task = baseMapper.selectById(taskId);
        if(null == task){
            ApprovalErrorEnum error = ApprovalErrorEnum.TASK_NOT_FOUND;
            throw new BusinessException(error.getCode(), error.getDesc());
        }
        Long currentProcessId = task.getCurrentProcess();
        return taskProcessService.updateStatus(currentProcessId, status, userId);
    }

    private ApprovalTask genApprovalTask(ApprovalModelVO approvalModelVO, ApprovalTask taskVO, LoginUser loginUser) {
        ApprovalTask task = new ApprovalTask();
        String taskName = taskVO.getName();
        if(StringUtils.isBlank(taskName)){
            taskName = approvalModelVO.getName();
        }
        task.setName(taskName);
        task.setType(approvalModelVO.getType());
        task.setUserCreate(loginUser.getId());
        task.setUserCreateName(loginUser.getRealName());
        task.setTenantId(approvalModelVO.getTenantId());
        task.setGmtCreate(new Date());
        task.setStatus(TaskStatusEnum.RUNNING.getCode());
        return task;
    }
}
