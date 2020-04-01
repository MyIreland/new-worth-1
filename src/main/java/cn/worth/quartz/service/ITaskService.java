package cn.worth.quartz.service;

import cn.worth.quartz.domain.Task;
import com.baomidou.mybatisplus.extension.service.IService;
import org.quartz.SchedulerException;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author chenxiaoqing
 * @since 2019-09-09
 */
public interface ITaskService extends IService<Task> {

    Boolean remove(Long id, Long userId);

    Boolean batchRemove(Long[] ids, Long userId);

    Boolean initSchedule() throws SchedulerException;

    Boolean changeStatus(Long jobId, String status, Long userId) throws SchedulerException;

    Boolean updateCron(Long jobId, String cron, Long userId) throws SchedulerException;

    Boolean updateTask(Task task, Long userId) throws SchedulerException;
}
