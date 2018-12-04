package net.htwater.whale.configuration;

import net.htwater.whale.entity.QueryJob;
import net.htwater.whale.service.IJobService;
import net.htwater.whale.util.ScheduleUtil;
import org.quartz.CronTrigger;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;

import java.util.List;

@Configuration
public class JobListener implements ApplicationListener<ContextRefreshedEvent> {
    @Autowired
    IJobService iJobService;
    @Autowired
    @Qualifier("Scheduler")
    private Scheduler scheduler;
    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        List<QueryJob> Jobs = iJobService.queryJob();
        try {
            scheduler.start();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        for (QueryJob job:Jobs){
            try {
                CronTrigger cronTrigger = ScheduleUtil.getCronTrigger(scheduler,job);
                JobKey jobKey = JobKey.jobKey(job.getJOB_NAME(),job.getJOB_Group());
               // scheduler.triggerJob(jobKey);
                if (cronTrigger==null){
                    iJobService.addJob(job.getJOB_CLASS_NAME(),job.getJOB_NAME(),job.getJOB_Group(),job.getCRON_EXPRESSION());
                }else {
                    iJobService.jobreschedule(job.getJOB_NAME(),job.getJOB_Group(),job.getCRON_EXPRESSION());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            scheduler.start();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }
}
