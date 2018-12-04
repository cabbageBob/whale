package net.htwater.whale.service.impl;


import net.htwater.sesame.htweb.datasource.annotation.UseDatasource;
import net.htwater.whale.entity.*;
import net.htwater.whale.mapper.ColorMapper;
import net.htwater.whale.mapper.PicMapper;
import net.htwater.whale.mapper.RainStationMapper;
import net.htwater.whale.service.IContourService;
import net.htwater.whale.service.ISettingService;
import net.htwater.whale.util.ContourUtil;

import org.meteoinfo.data.StationData;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
/**
 * @author shitianlong
 * @since 2018-09-04
 */
@Service
public class ContourServiceImpl implements IContourService {
    private static final int INTERPOLATION_LEVEL_COUNT_MAX=10;
    private static final int INTERPOLATION_LEVEL_COUNT_MIN=4;
    private static final double INTERVAL_VALUE_MAX=10000.0;
    private static final double DIFFERENCE_BIG_SMALL_THRESHOLD=100.0;
    private static final String COMMA=",";
    private static final Double[] NUMBERS_CONTAINS_5={0.5,5.0,50.0,500.0,5000.0};
    private static final Double[] NUMBERS_CONTAINS_1={0.1,1.0,10.0,100.0,1000.0};
    @Autowired
    RainStationMapper rainStationMapper;
    @Autowired
    ColorMapper colorMapper;
    @Autowired
    ISettingService service;
    @Autowired
    PicMapper picMapper;
    @Override
    public String drawContourFeature(StationData data, double[] breaks, String district, Color[] colors, int type,int duration) {
        return ContourUtil.drawContourFeatureTemp(district,data,breaks,colors,type,duration);
    }

    @Override
    public List<Interval> listContourLevelsByExtreme(double min, double max, Long colorBand) {
        int level=INTERPOLATION_LEVEL_COUNT_MIN;
        double difference=max-min;
        double base=0.0;
        double interval=0.0;
        List<Double> fittedLength=new ArrayList<>();
        while(interval<INTERVAL_VALUE_MAX){
            if(interval<1.0){
                interval+=0.1;
                if(interval>=0.6){
                    interval=1.0;
                }
            }else if(interval<10){
                interval+=1;
            }else if(interval<100){
                interval+=10;
            }else {
                interval+=100;
            }
            double temp=difference/interval;
            if(temp>=INTERPOLATION_LEVEL_COUNT_MIN && temp<=INTERPOLATION_LEVEL_COUNT_MAX){
                fittedLength.add(interval);
            }
            if(fittedLength.size()>0 && temp<INTERPOLATION_LEVEL_COUNT_MIN){
                break;
            }
        }
        interval=0.0;
        for(double number:NUMBERS_CONTAINS_5){
            if(fittedLength.contains(number)){
                interval=number;
            }
        }
        if(interval==0.0){
            for(double number:NUMBERS_CONTAINS_1){
                if(fittedLength.contains(number)){
                    interval=number;
                }
            }
            if(interval==0.0){
                if(difference>DIFFERENCE_BIG_SMALL_THRESHOLD){
                    interval=fittedLength.get(0);
                }else{
                    interval=fittedLength.get(fittedLength.size()-1);
                }
            }
        }
        if(interval<1.0){
            base=Math.floor(min/0.1)*0.1;
        }
        for (int i = 1; i <NUMBERS_CONTAINS_1.length ; i++) {
            if(interval<NUMBERS_CONTAINS_1[i]){
                base=(i==1)
                        ?Math.floor(min/NUMBERS_CONTAINS_1[i])*NUMBERS_CONTAINS_1[i]
                        :Math.floor(min/NUMBERS_CONTAINS_5[i-2])*NUMBERS_CONTAINS_5[i-2];
            }
        }
        while(base+level*interval<max){
            level++;
        }
        List<Interval> intervals=new ArrayList<>();
        List<net.htwater.whale.entity.Color> colors=service.listColorsBySeriesId(colorBand);
        for (int i=0;i<level;i++){
            Interval bean=new Interval();
            bean.setMax(min+(i+1)*interval);
            bean.setMin(min+i*interval);
            bean.setColor(colors.get(i).getColor());
            intervals.add(bean);
        }
        return intervals;
    }

    @UseDatasource("data")
    @Override
    public List<StationRain> listContourStations(String startTime, String endTime, String include) {
        return rainStationMapper.getStationRainByTime(startTime,endTime,include);
    }
    @UseDatasource("data")
    @Override
    public List<StationRain> listAllRainStation(String startTime, String endTime) {
        return rainStationMapper.getAllStationRainByTime(startTime,endTime);
    }

    @Override
    public Picture getLatestPictureByType(String type) {
        return picMapper.getTodayPictureByType(type);
    }

    @Override
    public Picture getPictureByTypeAndDate(String type, String date) {
        date+=" 08:00:00";
        return picMapper.getPictureByTypeAndDate(type,date);
    }

    @Override
    public List<Picture> getContourPicturePath(String type, String begin, String end) {
        List<Picture> lists = picMapper.getPicByTimeAndType(type,begin,end);
        for (Picture picture :lists){
            String[] time = picture.getGenerated_time().split("\\s");
            picture.setGenerated_time(time[0]);
        }
        return lists;
    }

    @Override
    public void addPicture(String pictureName, String path, String time, String type) {
        picMapper.savePicInfo(pictureName,path,time,type);
    }

    private List<Interval> setIntervalColor(List<Interval> intervals,Long colorBand){
        List<net.htwater.whale.entity.Color> colors=service.listColorsBySeriesId(colorBand);
        List<Interval> result=new ArrayList<>();
        for (int i=0;i<intervals.size();i++){
            Interval interval=intervals.get(i);
            interval.setColor(colors.get(i).getColor());
            result.add(interval);
        }
        return result;
    }
}
