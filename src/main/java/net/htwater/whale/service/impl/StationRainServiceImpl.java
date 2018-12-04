package net.htwater.whale.service.impl;

import net.htwater.sesame.htweb.datasource.annotation.UseDatasource;
import net.htwater.whale.entity.RainStation;
import net.htwater.whale.mapper.RainStationMapper;
import net.htwater.whale.service.StationRainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author: Jokki
 * @Description:
 * @Date: Created in 11:05.2018/9/18
 * @Modified By:
 */
@Service
public class StationRainServiceImpl implements StationRainService {
    @Autowired
    private RainStationMapper rainStationMapper;

    @Override
    @UseDatasource("data")
    public int getStationSumRain(Date tm1, Date tm2) {
        return rainStationMapper.getRegionRain(tm1, tm2);
    }

    @Override
    @UseDatasource("data")
    public Map<String, Date> getMaxTm() {
        Map<String, Date> ra = rainStationMapper.getMaxTm();
        System.out.println();
        return ra;
    }

    @Override
    @UseDatasource("data")
    public Map<String, Date> getLastCalculateTm() {
        return rainStationMapper.getLastCalculateTm();
    }

    @Override
    @UseDatasource("data")
    public int updateCalculateTm(Date tm) {
        return rainStationMapper.updateCalculateTm(tm);
    }

    @Override
    @UseDatasource("data")
    public List<RainStation> listRainStationByFrgrd(String frgrds) {
        return rainStationMapper.listRainStationByFrgrd(frgrds);
    }
}
