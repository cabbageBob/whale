
package net.htwater.whale.controller;

import com.google.common.base.Strings;
import net.htwater.sesame.htweb.common.dto.ApiResponse;
import net.htwater.sesame.htweb.common.validator.ValidatorUtils;
import net.htwater.whale.entity.*;
import net.htwater.whale.entity.param.ContourGeoJsonParams;
import net.htwater.whale.entity.param.ContourRainsParams;
import net.htwater.whale.service.IContourService;
import net.htwater.whale.service.ISettingService;
import net.htwater.whale.service.RegionRainService;
import net.htwater.whale.util.ContourUtil;
import net.htwater.whale.util.DateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @author shitianlong
 * @since  2018-09-01
 */
@RestController
@Validated
@RequestMapping(value="/contour")
public class ContourController {
    private static final String COMMA=",";
    @Autowired
    private IContourService service;
    @Autowired
    private ISettingService settingService;
    @Autowired
    private RegionRainService regionRainService;

    @RequestMapping(value="/rain/event",method=RequestMethod.POST,produces="application/json;charset=UTF-8")
    public ApiResponse<Map<String,Object>> listContourRains(@RequestBody ContourRainsParams params){
        ValidatorUtils.validatorEntity(params);
        Map<String,Object>result=new HashMap<>(2);
        List<StationRain> rains=service.listContourStations(params.getStartTime(), params.getEndTime(), params.getStations());
        result.put("rain",rains);
        if(params.getType()<0){
            List<FixedIntervals> fixedIntervals=settingService.listFixedIntervals(-1*params.getType());
            result.put("colors",fixedIntervals);
        }else{
            double[] d=getMinAndMax(rains);
            List<Interval> intervalList=service.listContourLevelsByExtreme(d[1],d[0],(long)params.getType());
            result.put("colors",intervalList);
        }
        return ApiResponse.success(result);
    }
    @RequestMapping(value = "/rain/year", method = RequestMethod.GET)
    public ApiResponse<List<RegionRain>> getRegionRainByYear (@RequestParam Integer year) {
        return ApiResponse.success(regionRainService.getRegionRainByYear(year));
    }
    @RequestMapping(value="/interval",method=RequestMethod.GET,produces="application/json;charset=UTF-8")
    public ApiResponse<List<Interval>> listContourInterval(@RequestParam("min") double min
            ,@RequestParam("max")double max
            ,@RequestParam("series")long colorSeries){
        return ApiResponse.success(service.listContourLevelsByExtreme(min,max,colorSeries));
    }
    @RequestMapping(value="/current/geojson",method=RequestMethod.POST,produces="application/json;charset=UTF-8")
    public ApiResponse<String> drawCurrentContourGeoJson(@RequestBody ContourGeoJsonParams params){
        return ApiResponse.success(service.drawContourFeature(params.getStationData(),params.getBreakArray()
                , params.getDistrict(),params.getColorArray(), params.getType(),DateTimeUtil.CUSTOM_TIME));
    }
    @RequestMapping(value="/current/picture",method=RequestMethod.POST,produces="application/json;charset=UTF-8")
    public ApiResponse<String> drawCurrentContourPicture(@RequestBody ContourGeoJsonParams params){
        return ApiResponse.success(service.drawContourFeature(params.getStationData(),params.getBreakArray()
                , params.getDistrict(),params.getColorArray(), ContourUtil.CONTOUR_POLYGON_PNG,DateTimeUtil.CUSTOM_TIME));
    }

    @RequestMapping(value ="/history/picture/series", method = RequestMethod.GET,produces="application/json;charset=UTF-8")
    public ApiResponse<Map<String,Object>> getContourPicturePath(@RequestParam("duration")String duration
            ,@RequestParam("begin")String begin,@RequestParam("end")String end){
        Map<String,Object> result=new HashMap<>(2);
        List<Picture> pictures=service.getContourPicturePath(duration,begin,end);
        List<String> names=new ArrayList<>();
        for (Picture picture:pictures){
            names.add(picture.getPic_name());
        }
        result.put("pictures",names);
        List<FixedIntervals> fixedIntervals=settingService.listFixedIntervals(DateTimeUtil.HYDROLOGICAL_DAY_1);
        result.put("colors",fixedIntervals);
        return ApiResponse.success(result);
    }

    @RequestMapping(value="/history/picture/single",method=RequestMethod.GET,produces="application/json;charset=UTF-8")
    public ApiResponse<Map<String,Object>> getConcurrentContourGeoJson(@RequestParam("duration") int type
            , @RequestParam(value = "date",required = false) String date) {
        Map<String,Object> result=new HashMap<>(2);
        List<FixedIntervals> fixedIntervals=settingService.listFixedIntervals(type);
        result.put("colors",fixedIntervals);
        if(Strings.isNullOrEmpty(date)){
            result.put("picture",service.getLatestPictureByType(String.valueOf(type)).getPic_name());
        }else{
            result.put("picture",service.getPictureByTypeAndDate(String.valueOf(type),date).getPic_name());
        }
        return ApiResponse.success(result);
    }

    private double[] getMinAndMax(List<StationRain> rains) {
        double[] result = {0, 50000};
        for (StationRain rain : rains) {
            if (rain.getValue() > result[0]) {
                result[0] = rain.getValue();
            }
            if (rain.getValue() < result[1]) {
                result[1] = rain.getValue();
            }
        }
        return result;
    }
}