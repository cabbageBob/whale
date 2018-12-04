package net.htwater.whale.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * @author wangzhifan
 */
public interface BaseJob extends Job {
    /**
     *execute job
     * @param var1 unknown
     * @throws JobExecutionException exception
     */
    @Override
    void execute(JobExecutionContext var1) throws JobExecutionException;
}
