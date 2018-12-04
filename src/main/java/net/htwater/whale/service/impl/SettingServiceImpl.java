package net.htwater.whale.service.impl;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import net.htwater.sesame.htweb.datasource.annotation.UseDatasource;
import net.htwater.whale.entity.*;
import net.htwater.whale.entity.param.ColorSettingParams;
import net.htwater.whale.mapper.*;
import net.htwater.whale.service.ISettingService;
import net.htwater.whale.util.ContourUtil;
import net.htwater.whale.util.DateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author shitianlong
 */
@Service
public class SettingServiceImpl implements ISettingService {
    private static final String RECOMMEND_COLOR_BAR="推荐色带";
    @Autowired
    ColorSeriesMapper colorSeriesMapper;
    @Autowired
    ColorMapper colorMapper;
    @Autowired
    StationSeriesMapper stationSeriesMapper;
    @Autowired
    StationMapper stationMapper;
    @Autowired
    RainStationMapper rainStationMapper;
    @Autowired
    FixedIntervalsMapper fixedIntervalsMapper;

    @Override
    public long addColorSeries(ColorSeries series, List<Color> colors) {
        if(series.getIsDefault()==1){
            colorSeriesMapper.clearDefaultSeries();
        }
        Date date=new Date();
        series.setGmtModified(date);
        series.setGmtCreate(date);
        colorSeriesMapper.insert(series);
        for (Color color: colors){
            color.setSeriesId(series.getId());
            color.setGmtCreate(date);
            color.setGmtModified(date);
            colorMapper.insert(color);
        }
        return series.getId();
    }


    @Override
    public boolean updateColorSeries(ColorSeries series) {
        series.setGmtModified(new Date());
        colorSeriesMapper.updateById(series);
        return true;
    }


    @Override
    public boolean updateColorsById(long id,List<Color> colors) {
        Date date=new Date();
        ColorSeries series=colorSeriesMapper.selectById(id);
        series.setGmtModified(date);
        colorSeriesMapper.updateById(series);
        EntityWrapper<Color> entityWrapper=new EntityWrapper<>();
        entityWrapper.where("series_id={0}",id);
        colorMapper.delete(entityWrapper);
        for (Color color: colors){
            color.setSeriesId(series.getId());
            color.setGmtCreate(date);
            color.setGmtModified(date);
            colorMapper.insert(color);
        }
        return true;
    }

    @Override
    public boolean deleteColorSeries(Long colorSeriesId) {
        EntityWrapper<Color> entityWrapper=new EntityWrapper<>();
        entityWrapper.where("series_id={0}",colorSeriesId);
        colorMapper.delete(entityWrapper);
        colorSeriesMapper.deleteById(colorSeriesId);
        return true;
    }


    @Override
    public List<ColorSeries> listColorSeries() {
        EntityWrapper<ColorSeries> entityWrapper=new EntityWrapper<>();
        return colorSeriesMapper.selectList(entityWrapper);
    }

    @Override
    public List<Color> listColorsBySeriesId(Long colorSeriesId) {
        if(colorSeriesId==0L){
            EntityWrapper<ColorSeries> e=new EntityWrapper<>();
            e.where("is_default={0}",1);
            ColorSeries series=colorSeriesMapper.selectList(e).get(0);
            colorSeriesId=series.getId();
        }
        EntityWrapper<Color> entityWrapper=new EntityWrapper<>();
        entityWrapper.where("series_id={0}",colorSeriesId);
        return colorMapper.selectList(entityWrapper);
    }

    @Override
    public long addStationSeries(StationSeries series, List<Station> stations) {
        if(series.getIsDefault()==1){
            stationSeriesMapper.clearDefaultSeries();
        }
        Date date=new Date();
        series.setGmtModified(date);
        series.setGmtCreate(date);
        stationSeriesMapper.insert(series);
        for (Station station: stations){
            station.setSeriesId(series.getId());
            station.setGmtCreate(date);
            station.setGmtModified(date);
            stationMapper.insert(station);
        }
        return series.getId();
    }

    @Override
    public boolean updateStationSeries(StationSeries series) {
        if (series.getIsDefault()==1){
            stationSeriesMapper.clearDefaultSeries();
        }
        series.setGmtModified(new Date());
        stationSeriesMapper.updateById(series);
        return true;
    }

    @Override
    public boolean updateStation(long id,List<Station> stations) {
        StationSeries series=stationSeriesMapper.selectById(id);
        series.setGmtModified(new Date());
        stationSeriesMapper.updateById(series);
        EntityWrapper<Station> entityWrapper=new EntityWrapper<>();
        entityWrapper.where("series_id={0}",id);
        stationMapper.delete(entityWrapper);
        Date date=new Date();
        for (Station station: stations){
            station.setSeriesId(series.getId());
            station.setGmtCreate(date);
            station.setGmtModified(date);
            stationMapper.insert(station);
        }
        return true;
    }

    @Override
    public boolean deleteStationSeries(Long seriesId) {
        EntityWrapper<Station> entityWrapper=new EntityWrapper<>();
        entityWrapper.where("series_id={0}",seriesId);
        stationMapper.delete(entityWrapper);
        stationSeriesMapper.deleteById(seriesId);
        return true;
    }

    @Override
    public List<StationSeries> listStationSeries() {
        EntityWrapper<StationSeries> entityWrapper=new EntityWrapper<>();
        return stationSeriesMapper.selectList(entityWrapper);
    }
    @Override
    public List<Station> listStations(Long seriesId) {
        if(seriesId==0L){
            EntityWrapper<StationSeries> e=new EntityWrapper<>();
            e.where("is_default={0}",1);
            StationSeries series=stationSeriesMapper.selectList(e).get(0);
            seriesId=series.getId();
        }
        EntityWrapper<Station> entityWrapper=new EntityWrapper<>();
        entityWrapper.where("series_id={0}",seriesId);
        return stationMapper.selectList(entityWrapper);

    }

    @UseDatasource("data")
    @Override
    public List<RainStation> listStationForVacuate(int kilometres) {
        if (kilometres==0){
            return rainStationMapper.listRainStation();
        }else{
            return ContourUtil.vacuateStations(rainStationMapper.listRainStation(),kilometres);
        }
    }

    @Override
    public List<ColorBar> listColorBar(String start, String end) {
        Date startDate=DateUtil.parse(start);
        Date endDate=DateUtil.parse(end);
        long betweenMinute=DateUtil.between(startDate,endDate,DateUnit.MINUTE);
        long betweenDay=DateUtil.between(startDate,endDate,DateUnit.DAY);
        int type=0;
        List<ColorBar> result=new ArrayList<>();
        if(betweenMinute>58&&betweenMinute<62){
            type=DateTimeUtil.HYDROLOGICAL_HOUR_1;
        }else if(betweenMinute>358&&betweenMinute<362){
            type=DateTimeUtil.HYDROLOGICAL_HOUR_6;
        }else if(betweenMinute>718&&betweenMinute<722){
            type=DateTimeUtil.HYDROLOGICAL_HOUR_12;
        }else if(betweenMinute>1438&&betweenMinute<1442){
            type=DateTimeUtil.HYDROLOGICAL_HOUR_24;
        }
        if(start.endsWith(DateTimeUtil.HOUR_OF_HYDROLOGICAL_DAY)){
            if(betweenDay==1){
                type=DateTimeUtil.HYDROLOGICAL_DAY_1;
            }else if(betweenDay==3){
                type=DateTimeUtil.HYDROLOGICAL_DAY_3;
            }else if(betweenDay==7){
                type=DateTimeUtil.HYDROLOGICAL_DAY_7;
            }else if(betweenDay==10){
                type=DateTimeUtil.HYDROLOGICAL_DAY_10;
            }
        }
        if (type!=0){
            List<FixedIntervals> intervals=listFixedIntervals(type);
            String[] colorsString=new String[intervals.size()];
            for (int i = 0; i <intervals.size() ; i++) {
                colorsString[i]=intervals.get(i).getColor();
            }
            ColorBar colorBar=new ColorBar(-1*type,RECOMMEND_COLOR_BAR,colorsString);
            result.add(colorBar);
        }
        List<ColorSeries> series=listColorSeries();
        for (ColorSeries c:series){
            List<Color> colors=listColorsBySeriesId(c.getId());
            String[] colorsString=new String[colors.size()];
            for (int i = 0; i <colors.size() ; i++) {
                colorsString[i]=colors.get(i).getColor();
            }
            ColorBar colorBar=new ColorBar(c.getId(),c.getName(),colorsString);
            result.add(colorBar);
        }
        return result;
    }
    @Override
    public List<FixedIntervals> listFixedIntervals(int type){
        EntityWrapper<FixedIntervals> entityWrapper=new EntityWrapper<>();
        entityWrapper.where("type={0}",type);
        return fixedIntervalsMapper.selectList(entityWrapper);
    }
}
