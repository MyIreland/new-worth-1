package cn.worth.quartz.config;

import cn.worth.quartz.service.ITaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @author myireland
 * @version 1.0.0
 * @date 2019-09-10
 * @description
 **/
@Slf4j
@Component
@Order(value = 1)
public class ScheduleJobInitListener implements CommandLineRunner {

    @Autowired
    private ITaskService taskService;

    @Override
    public void run(String... arg0) throws Exception {
        log.info("======================= init schedule beginning ==================");
        try {
            taskService.initSchedule();
        } catch (Exception e) {
            log.error("======================= init schedule error: {}", e.getMessage());
        }
        log.info("======================= init schedule finished ==================");
    }

}