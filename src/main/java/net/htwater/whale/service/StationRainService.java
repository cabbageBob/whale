package net.htwater.whale.service;

import net.htwater.whale.entity.RainStation;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Jokki
 */
public interface StationRainService {

    /**
     * 获取站点统计雨量
     * @param tm1 开始时间
     * @param tm2 结束时间
     * @return int
     */
    int getStationSumRain(Date tm1, Date tm2);

    /**
     * 获取降雨最大出现时间
     * @return map
     */
    Map<String, Date> getMaxTm();

    /**
     * 获取最后计算时间
     * @return map
     */
    Map<String, Date>getLastCalculateTm();

    /**
     * 更新计算时间
     * @param tm 时间
     * @return int
     */
    int updateCalculateTm(Date tm);

    /**
     * @param frgrds 报汛站等级
     */
    List<RainStation> listRainStationByFrgrd(String frgrds);
}
