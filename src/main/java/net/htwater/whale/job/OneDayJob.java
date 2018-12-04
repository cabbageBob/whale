package net.htwater.whale.job;

import net.htwater.whale.service.IGenerateContourPictureService;
import net.htwater.whale.util.DateTimeUtil;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author wangzhifan
 */
public class OneDayJob implements BaseJob {
    @Autowired
    IGenerateContourPictureService service;
    private static Logger log = LoggerFactory.getLogger(OneDayJob.class);
    @Override
    public void execute(JobExecutionContext context){
        log.info("开始生成日等值面图片 7日...");
        service.generateContourPicture(DateTimeUtil.HYDROLOGICAL_DAY_7);
        log.info("开始生成日等值面图片 3日...");
        service.generateContourPicture(DateTimeUtil.HYDROLOGICAL_DAY_3);
        log.info("开始生成日等值面图片 1日...");
        service.generateContourPicture(DateTimeUtil.HYDROLOGICAL_DAY_1);
        log.info("日等值面图片生成成功...");
    }
}
