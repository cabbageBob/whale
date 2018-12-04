package net.htwater.whale.mapper;

import net.htwater.whale.entity.QueryJob;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface JobMapper {
    @Select(" SELECT qrtz_job_details.JOB_NAME,qrtz_job_details.JOB_GROUP,qrtz_job_details.JOB_CLASS_NAME," +
            " qrtz_triggers.TRIGGER_NAME,qrtz_triggers.TRIGGER_GROUP,qrtz_cron_triggers.CRON_EXPRESSION " +
            " from qrtz_job_details join qrtz_triggers join qrtz_cron_triggers on qrtz_triggers.JOB_NAME=" +
            " qrtz_job_details.JOB_NAME and qrtz_triggers.TRIGGER_NAME = qrtz_cron_triggers.TRIGGER_NAME" +
            " and qrtz_triggers.TRIGGER_GROUP =  qrtz_cron_triggers.TRIGGER_GROUP")
    public List<QueryJob> queryJobs();
}
