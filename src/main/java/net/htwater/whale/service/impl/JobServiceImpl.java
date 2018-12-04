package net.htwater.whale.service.impl;

import net.htwater.whale.entity.QueryJob;
import net.htwater.whale.job.BaseJob;
import net.htwater.whale.mapper.JobMapper;
import net.htwater.whale.service.IJobService;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobServiceImpl implements IJobService {
    @Autowired
    JobMapper jobMapper;
    @Autowired
    @Qualifier("Scheduler")
    private Scheduler scheduler;


    @Override
    public List<QueryJob> queryJob() {
        return jobMapper.queryJobs();
    }

    public void addJob( String jobClassName,String jobName, String jobGroupName, String cronExpression) throws Exception {
        JobDetail jobDetail = JobBuilder.newJob(getClass(jobClassName).getClass()).withIdentity(jobName,jobGroupName).build();
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);
        CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(jobName,jobGroupName)
                .withSchedule(scheduleBuilder).build();
        try {
            scheduler.scheduleJob(jobDetail, trigger);
        }catch (SchedulerException  e){
            System.out.println("创建定时任务失败"+e);
            throw new Exception("创建定时任务失败");
        }
    }

//    public void addSimpleJob( String jobClassName,String jobName, String jobGroupName, String cronExpression) throws Exception{
//        JobDetail jobDetail = JobBuilder.newJob(getClass(jobClassName).getClass()).withIdentity(jobName,jobGroupName).build();
//        java.util.Calendar cal = java.util.Calendar.getInstance();
//        cal.set(java.util.Calendar.HOUR_OF_DAY, 8);
//        cal.set(java.util.Calendar.MINUTE, 00);
//        cal.set(java.util.Calendar.SECOND, 00);
//        Date todayHalfToFour = cal.getTime();
//        SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.simpleSchedule();
//        SimpleTrigger trigger = TriggerBuilder.newTrigger().withIdentity(jobName,jobGroupName).startAt(todayHalfToFour)
//                                .withSchedule(scheduleBuilder.withIntervalInHours(72)).build();
//        try {
//            scheduler.scheduleJob(jobDetail, trigger);
//            scheduler.start();
//        }catch (SchedulerException  e){
//            System.out.println("创建定时任务失败"+e);
//            throw new Exception("创建定时任务失败");
//        }
//    }

    public void jobPause(String jobName, String jobGroupName) throws Exception {
        scheduler.pauseJob(JobKey.jobKey(jobName, jobGroupName));
    }

    public void jobreschedule(String jobName, String jobGroupName, String cronExpression) throws Exception
    {
        try {
            TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroupName);
            // 表达式调度构建器
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);

            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);

            // 按新的cronExpression表达式重新构建trigger
            trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();

            // 按新的trigger重新设置job执行
            scheduler.rescheduleJob(triggerKey, trigger);
        } catch (SchedulerException e) {
            System.out.println("更新定时任务失败"+e);
            throw new Exception("更新定时任务失败");
        }
    }

    public void jobresume(String jobName, String jobGroupName) throws Exception
    {
        scheduler.resumeJob(JobKey.jobKey(jobName, jobGroupName));
    }

    public void jobdelete(String jobName, String jobGroupName) throws Exception
    {
        scheduler.pauseTrigger(TriggerKey.triggerKey(jobName, jobGroupName));
        scheduler.unscheduleJob(TriggerKey.triggerKey(jobName, jobGroupName));
        scheduler.deleteJob(JobKey.jobKey(jobName, jobGroupName));
    }

    private static BaseJob getClass(String classname) throws Exception
    {
        Class<?> class1 = Class.forName(classname);
        return (BaseJob)class1.newInstance();
    }
}
