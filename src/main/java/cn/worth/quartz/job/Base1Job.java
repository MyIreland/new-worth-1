package cn.worth.quartz.job;

import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class Base1Job implements Job {

    @Override
    public void execute(JobExecutionContext arg0) throws JobExecutionException {
        log.info("job1 + 定时器触发机关");
    }
}