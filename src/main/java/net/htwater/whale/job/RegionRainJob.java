package net.htwater.whale.job;

import lombok.extern.slf4j.Slf4j;
import net.htwater.whale.service.RegionRainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author Jokki
 * @since 2018/9/18
 */
@Slf4j
@Component
@EnableScheduling
public class RegionRainJob{

    @Autowired
    private RegionRainService regionRainService;

    @Scheduled(cron = "0 0/10 * * * *")
    public void execute() {
        log.info("日面雨量刷新开始....");
        regionRainService.refreshRegionDayRain();
        log.info("日面雨量刷新成功....");
    }
}
