package net.htwater.whale.service;

import net.htwater.whale.entity.QueryJob;

import java.util.List;

public interface IJobService {
    List<QueryJob> queryJob();
    void addJob( String jobClassName,String jobName, String jobGroupName, String cronExpression)throws Exception;
//    void addSimpleJob( String jobClassName,String jobName, String jobGroupName, String cronExpression) throws Exception;
    void jobPause(String jobClassName, String jobGroupName) throws Exception;
    void jobreschedule(String jobClassName, String jobGroupName, String cronExpression)throws Exception;
    void jobresume(String jobClassName, String jobGroupName) throws Exception;
    void jobdelete(String jobClassName, String jobGroupName) throws Exception;
}
