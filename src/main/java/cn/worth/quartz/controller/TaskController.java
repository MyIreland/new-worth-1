package cn.worth.quartz.controller;

import cn.worth.common.annotation.CurrentUser;
import cn.worth.common.constant.CommonConstant;
import cn.worth.common.domain.R;
import cn.worth.common.utils.StringUtils;
import cn.worth.core.controller.BaseController;
import cn.worth.core.domain.LoginUser;
import cn.worth.quartz.domain.Task;
import cn.worth.quartz.service.ITaskService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author chenxiaoqing
 * @since 2019-09-09
 */
@RestController
@RequestMapping("/task")
public class TaskController extends BaseController<ITaskService, Task> {

    @Autowired
    private ITaskService taskService;

    /**
     * 通过ID查询
     *
     * @param id ID
     * @return Task
     */
    @GetMapping("/{id}")
    public R<Task> get(@PathVariable Long id) {
        return R.success(taskService.getById(id));
    }


    /**
     * 分页查询信息
     *
     * @return 分页对象
     */
    @RequestMapping("/page")
    public R page(Page<Task> entityPage, Task entity) {
        QueryWrapper<Task> wrapper = new QueryWrapper<>();
        wrapper.eq("del_flag", CommonConstant.STATUS_NORMAL);
        String jobName = entity.getJobName();
        if(StringUtils.isNotBlank(jobName)){
            wrapper.like("job_name", jobName);
        }
        String jobGroup = entity.getJobGroup();
        if(StringUtils.isNotBlank(jobGroup)){
            wrapper.eq("job_group", jobGroup);
        }
        String jobStatus = entity.getJobStatus();
        if(StringUtils.isNotBlank(jobStatus)){
            wrapper.eq("job_status", jobStatus);
        }
        wrapper.orderByDesc("id");

        Page<Task> page = taskService.page(entityPage, wrapper);
        return R.success(page);
    }

    /**
     * 添加
     *
     * @param task 实体
     * @return success/false
     */
    @PostMapping
    public R<Boolean> add(@RequestBody Task task) {
        return R.success(taskService.save(task));
    }

    /**
     * 删除
     *
     * @param id ID
     * @return success/false
     */
    @DeleteMapping("/{id}")
    public R<Boolean> delete(@PathVariable Long id, @CurrentUser LoginUser loginUser) {
        return new R<>(taskService.remove(id, loginUser.getId()));
    }

    /**
     * 编辑
     *
     * @param task 实体
     * @return success/false
     */
    @PutMapping
    public R<Boolean> edit(@RequestBody Task task, @CurrentUser LoginUser loginUser) throws SchedulerException {
        return new R<>(taskService.updateTask(task, loginUser.getId()));
    }

    @PostMapping("batchRemove")
    public R<Boolean> batchRemove(@RequestBody Long[] ids, @CurrentUser LoginUser loginUser) throws SchedulerException {
        return new R<>(taskService.batchRemove(ids, loginUser.getId()));
    }
    @PostMapping("changeStatus")
    public R<Boolean> changeStatus(Long jobId, String status, @CurrentUser LoginUser loginUser) throws SchedulerException {
        return new R<>(taskService.changeStatus(jobId, status, loginUser.getId()));
    }

    @PostMapping("updateCron")
    public R<Boolean> updateCron(Long jobId, String cron, @CurrentUser LoginUser loginUser) throws SchedulerException {
        return new R<>(taskService.updateCron(jobId, cron, loginUser.getId()));
    }
}
