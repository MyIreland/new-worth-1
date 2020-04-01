package cn.worth.quartz.service.impl;

import cn.worth.common.constant.CommonConstant;
import cn.worth.common.exception.BusinessException;
import cn.worth.common.utils.StringUtils;
import cn.worth.quartz.component.QuartzService;
import cn.worth.quartz.domain.Task;
import cn.worth.quartz.enums.JobStatusEnum;
import cn.worth.quartz.mapper.TaskMapper;
import cn.worth.quartz.service.ITaskService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author chenxiaoqing
 * @since 2019-09-09
 */
@Slf4j
@Service
public class TaskServiceImpl extends ServiceImpl<TaskMapper, Task> implements ITaskService {

    @Autowired
    private QuartzService quartzService;

    @Override
    public Boolean remove(Long id, Long userId) {
        try {
            log.info("remove task:{}, user:{}", id, userId);
            Task scheduleJob = baseMapper.selectById(id);

            quartzService.deleteJob(scheduleJob);

            return delEntity(userId, scheduleJob);
        } catch (SchedulerException e) {
            log.error("remove task:{}, error: {}", id, e.getMessage());
            return false;
        }
    }

    private boolean delEntity(Long userId, Task scheduleJob) {
        scheduleJob.setDelFlag(CommonConstant.STATUS_DEL);
        scheduleJob.setGmtUpdate(new Date());
        scheduleJob.setUpdateUser(userId);
        return updateById(scheduleJob);
    }

    @Override
    public Boolean batchRemove(Long[] ids, Long userId) {
        for (Long id : ids) {
            remove(id, userId);
        }
        return true;
    }

    @Override
    public Boolean initSchedule() throws SchedulerException {
        // 这里获取任务信息数据
        List<Task> tasks = listRunningTask();
        for (Task task : tasks) {
            quartzService.addJob(task);
        }
        return true;
    }

    private List<Task> listRunningTask(){
        QueryWrapper<Task> QueryWrapper = new QueryWrapper<>();
        QueryWrapper.eq("del_flag", CommonConstant.STATUS_NORMAL);
        QueryWrapper.eq("job_status", JobStatusEnum.RUNNING.getCode());
        return baseMapper.selectList(QueryWrapper);
    }

    @Override
    public Boolean changeStatus(Long jobId, String status, Long userId) throws SchedulerException {
        verifyParam(jobId, status);
        Task task = getById(jobId);
        if (task == null) {
            return true;
        }
        String notRunningCode = JobStatusEnum.NOT_RUNNING.getCode();
        String runningCode = JobStatusEnum.RUNNING.getCode();
        if (notRunningCode.equals(status)) {
            quartzService.deleteJob(task);
        } else if(runningCode.equals(status)) {
            quartzService.addJob(task);
        }
        task.setJobStatus(status);
        task.setUpdateUser(userId);
        task.setGmtUpdate(new Date());
        updateById(task);
        return true;
    }

    private void verifyParam(Long jobId, String status) {
        if(null == jobId){
            throw new BusinessException("jobId is null");
        }
        if(StringUtils.isBlank(status)){
            throw new BusinessException("status is null");
        }
    }

    @Override
    public Boolean updateCron(Long jobId, String cron, Long userId) throws SchedulerException {
        Task task = getById(jobId);
        if (task == null) {
            return true;
        }
        if (JobStatusEnum.RUNNING.getCode().equals(task.getJobStatus()) && !task.getCronExpression().equals(cron)) {
            task.setCronExpression(cron);
            quartzService.updateJobCron(task);
        }
        updateById(task);
        return true;
    }

    @Override
    public Boolean updateTask(Task task, Long userId) throws SchedulerException {
        task.setUpdateUser(userId);
        task.setGmtUpdate(new Date());
        String jobStatus = task.getJobStatus();
        if(JobStatusEnum.RUNNING.getCode().equals(jobStatus)){
            quartzService.updateJobCron(task);
        }
        return updateById(task);
    }
}
