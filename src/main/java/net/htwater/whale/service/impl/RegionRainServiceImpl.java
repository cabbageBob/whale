package net.htwater.whale.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import net.htwater.sesame.htweb.datasource.annotation.UseDatasource;
import net.htwater.whale.entity.RegionRain;
import net.htwater.whale.entity.param.TimeInterval;
import net.htwater.whale.mapper.RegionRainMapper;
import net.htwater.whale.service.RegionRainService;
import net.htwater.whale.service.StationRainService;
import net.htwater.whale.util.DateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author: Jokki
 * @Description:
 * @Date: Created in 13:43.2018/9/18
 * @Modified By:
 */
@Service
public class RegionRainServiceImpl extends ServiceImpl<RegionRainMapper, RegionRain> implements RegionRainService {

    @Autowired
    private StationRainService rainService;

    @Override
    @UseDatasource("data")
    public void calculateRegionDayRain(LocalDateTime begin, LocalDateTime end) {
        LocalDateTime tm1 = begin;
        List<TimeInterval> timeIntervals = Lists.newArrayList();
        while (tm1.compareTo(end) <= 0 && tm1.compareTo(DateTimeUtil.getToday8H()) <= 0) {
            timeIntervals.add(new TimeInterval( Date.from(tm1.atZone(ZoneId.systemDefault()).toInstant()),
                    Date.from(tm1.plusDays(1).atZone(ZoneId.systemDefault()).toInstant())));
            tm1 = tm1.plusDays(1);
        }
        timeIntervals.parallelStream().forEach(timeInterval ->
            rainService.getStationSumRain(timeInterval.getTm1(), timeInterval.getTm2())
        );
    }

    @Override
    @UseDatasource("data")
    public List<RegionRain> getRegionRainByYear(Integer year) {
        return selectList(new EntityWrapper<RegionRain>()
                .ge("tm", year + "-01-01")
                .and().le("tm", year + "-12-31"));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @UseDatasource("data")
    public  void  refreshRegionDayRain() {
        Map<String, Date> maxMin = rainService.getMaxTm();
        Map<String, Date> cal = rainService.getLastCalculateTm();
        if (maxMin != null) {
            LocalDateTime tm1;
            //最大时间
            LocalDateTime maxTm = DateTimeUtil.getToday8H(maxMin.get("maxTm"));
            if (cal != null) {
                //上次计算的最大时间
                LocalDateTime calTm = DateTimeUtil.getToday8H(cal.get("calTm"));
                tm1 = maxTm.isAfter(calTm) ? calTm : maxTm;
            } else {
                //初始需要很长时间
                //最小时间
                tm1 = DateTimeUtil.getToday8H(maxMin.get("minTm"));
            }
            //计算日面雨量
            calculateRegionDayRain(tm1, maxTm);
            //最后更新计算时的最大时间
            rainService.updateCalculateTm(Date.from(maxTm.atZone(ZoneId.systemDefault()).toInstant()));
        }
    }

}
