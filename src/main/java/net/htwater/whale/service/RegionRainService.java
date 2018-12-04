package net.htwater.whale.service;

import com.baomidou.mybatisplus.service.IService;
import net.htwater.whale.entity.RegionRain;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Jokki
 * @date Created in 15:33.2018/9/17
 */

public interface RegionRainService extends IService<RegionRain> {

    /**
     * 计算日面雨量
     * @param begin 开始时间
     * @param end 结束时间
     */
    void calculateRegionDayRain(LocalDateTime begin, LocalDateTime end);

    /**
     * 根据年份获取一年每日降雨量
     * @param year 年
     * @return 面雨量
     */
    List<RegionRain> getRegionRainByYear(Integer year);

    /**
     * 刷新日面雨量
     */
    void refreshRegionDayRain();

}
