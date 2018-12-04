package net.htwater.whale.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @Author: Jokki
 * @Description:
 * @Date: Created in 9:47.2018/9/18
 * @Modified By:
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RegionRainServiceTest {
    @Autowired
    private RegionRainService rainService;
    @Test
    public void calculateRegionDayRain() throws Exception {
        rainService.calculateRegionDayRain(LocalDateTime.parse("2018-01-01 08:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                LocalDateTime.parse("2018-01-02 08:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    }

    @Test
    public void refreshRegionDayRainTest() throws Exception {
        rainService.refreshRegionDayRain();
    }

}