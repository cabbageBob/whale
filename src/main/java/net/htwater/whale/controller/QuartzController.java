package net.htwater.whale.controller;


import net.htwater.whale.entity.QueryJob;
import net.htwater.whale.service.IJobService;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author wangzhifan
 * @since 2018-09-15
 */
@RestController
@RequestMapping(value = "/job")
public class QuartzController {
    @Autowired
    IJobService iJobService;
    @Autowired
    @Qualifier("Scheduler")
    private Scheduler scheduler;
    private static Logger log = LoggerFactory.getLogger(QuartzController.class);

    @PostMapping(value = "/pausejob")
    public void pauseJob(@RequestParam(value="jobName")String jobName,
                         @RequestParam(value="jobGroupName")String jobGroupName) throws Exception {
        iJobService.jobPause(jobName, jobGroupName);
    }

    @PostMapping(value = "/reschedulejob")
    public void rescheduleJob(@RequestParam(value="jobName")String jobName,
                              @RequestParam(value="jobGroupName")String jobGroupName,
                              @RequestParam(value="cronExpression")String cronExpression) throws Exception {
        iJobService.jobreschedule(jobName, jobGroupName, cronExpression);
    }

    @PostMapping(value="/resumejob")
    public void resumejob(@RequestParam(value="jobName")String jobName,
                          @RequestParam(value="jobGroupName")String jobGroupName) throws Exception
    {
        iJobService.jobresume(jobName, jobGroupName);
    }

    @PostMapping(value="/deletejob")
    public void deletejob(@RequestParam(value="jobName")String jobName, @RequestParam(value="jobGroupName")String jobGroupName) throws Exception
    {
        iJobService.jobdelete(jobName, jobGroupName);
    }

    @GetMapping(value="/queryjob")
    public List<QueryJob> queryjob()
    {
        return  iJobService.queryJob();
    }
}
