package net.htwater.whale.util;

import com.google.protobuf.ServiceException;
import net.htwater.whale.entity.QueryJob;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * @author wangzhifan
 */
public class ScheduleUtil {
    @Autowired
    @Qualifier("Scheduler")
    private Scheduler scheduler;
    private final static Logger logger = LoggerFactory.getLogger(ScheduleUtil.class);

    /**
     * 获取 Trigger Key
     * @param queryJob
     * @return TriggerKey
     */
    public static TriggerKey getTriggerKey(QueryJob queryJob) {
        return TriggerKey.triggerKey(queryJob.getTRIGGER_NAME(), queryJob.getTRIGGER_GROUP());
    }

    /**
     * get Job Key
     * @param queryJob
     * @return JobKey
     */
    public static JobKey getJobKey(QueryJob queryJob) {
        return JobKey.jobKey(queryJob.getJOB_NAME(), queryJob.getJOB_Group());
    }

    /**
     * get cron trigger
     * @param scheduler  scheduler
     * @param queryJob queryJob
     * @returnCronTrigger
     */
    public static CronTrigger getCronTrigger(Scheduler scheduler,QueryJob queryJob) throws ServiceException {
        try {
            return (CronTrigger)scheduler.getTrigger(getTriggerKey(queryJob));
        } catch (SchedulerException e) {
            throw new ServiceException("Get Cron trigger failed", e);
        }
    }
}
