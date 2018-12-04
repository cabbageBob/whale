package net.htwater.whale.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import net.htwater.sesame.htweb.common.dto.ApiResponse;
import net.htwater.whale.entity.*;
import net.htwater.whale.entity.param.ColorSettingParams;
import net.htwater.whale.entity.param.StationSettingParams;
import net.htwater.whale.service.ISettingService;
import net.htwater.whale.service.StationRainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author  shitianlong
 */

@RestController
@Validated
@RequestMapping(value="/setting")
public class SettingController {
    public static final int MAX_DISTANCE=1000;
    @Autowired
    ISettingService service;
    @Autowired
    StationRainService rainService;
    @RequestMapping(value="/listStationSeries",method=RequestMethod.GET,produces="application/json;charset=UTF-8")
    public ApiResponse<List<StationSeries>> listStationSeries(){
        return ApiResponse.success(service.listStationSeries());
    }
    @RequestMapping(value="/listStationsById",method=RequestMethod.POST,produces="application/json;charset=UTF-8")
    public ApiResponse<List<RainStation>> listStationsById(@RequestParam("id")long id){
        if(id==-1L){
            return ApiResponse.success(service.listStationForVacuate(0));
        }else {
            List<RainStation> rainStations = service.listStationForVacuate(0);
            List<Station> stations = service.listStations(id);
            for (RainStation rainStation : rainStations) {
                for (Station station : stations) {
                    if (station.getStcd() != null && station.getStcd().equals(rainStation.getStcd())) {
                        rainStation.setSelected(true);
                        break;
                    }
                }
            }
            return ApiResponse.success(rainStations);
        }
    }
    @RequestMapping(value="/addStationSeries",method=RequestMethod.POST,produces="application/json;charset=UTF-8")
    public ApiResponse<Long> addStationSeries(@RequestBody StationSettingParams params){
        return ApiResponse.success(service.addStationSeries(params.transformToStationSeries(),params.listStations()));
    }
    @RequestMapping(value="/updateStationSeries",method=RequestMethod.GET,produces="application/json;charset=UTF-8")
    public ApiResponse<Boolean> updateStationSeries(@RequestParam("id")long id,@RequestParam("name")String name
            ,@RequestParam("choose")boolean choose){
        StationSeries series=new StationSeries();
        series.setId(id);
        series.setName(name);
        series.setIsDefault(choose?1:0);
        return ApiResponse.success( service.updateStationSeries(series));
    }
    @RequestMapping(value="/updateStationsById",method=RequestMethod.POST,produces="application/json;charset=UTF-8")
    public ApiResponse<Boolean> updateStationsById(@RequestBody StationSettingParams params){
        return ApiResponse.success(service.updateStation(params.getId(),params.listStations()));
    }
    @RequestMapping(value="/deleteStationSeries",method=RequestMethod.GET,produces="application/json;charset=UTF-8")
    public ApiResponse<Boolean> deleteStationSeries(@RequestParam("id")long id){
        return ApiResponse.success(service.deleteStationSeries(id));
    }
    @RequestMapping(value="/listEquidistributionStation",method=RequestMethod.POST,produces="application/json;charset=UTF-8")
    public ApiResponse<List<RainStation>> listEquidistributionStation(@RequestParam("kilometres")int kilometres){
        if(kilometres>=0 && kilometres<MAX_DISTANCE){
            return ApiResponse.success(service.listStationForVacuate(kilometres));
        }else{
            return ApiResponse.error("kilometers should bigger than 0 and smaller than 500");
        }
    }
    @RequestMapping(value="/listColorSeries",method=RequestMethod.GET,produces="application/json;charset=UTF-8")
    public ApiResponse<List<ColorSeries>> listColorSeries(){
        return ApiResponse.success(service.listColorSeries());
    }
    @RequestMapping(value="/listColorsById",method=RequestMethod.GET,produces="application/json;charset=UTF-8")
    public ApiResponse<List<Color>> listColorsById(@RequestParam("id")long id){
        return ApiResponse.success(service.listColorsBySeriesId(id));
    }
    @RequestMapping(value="/addColorSeries",method=RequestMethod.GET,produces="application/json;charset=UTF-8")
    public ApiResponse<Long> addColorSeries(@RequestBody ColorSettingParams params){
        return ApiResponse.success(service.addColorSeries(params.transformToSeries(),params.listColors()));
    }
    @RequestMapping(value="/updateColorSeries",method=RequestMethod.GET,produces="application/json;charset=UTF-8")
    public ApiResponse<Boolean> updateColorSeries(@RequestBody ColorSettingParams params){
        return ApiResponse.success(service.updateColorSeries(params.transformToSeries()));
    }
    @RequestMapping(value="/updateColorsById",method=RequestMethod.GET,produces="application/json;charset=UTF-8")
    public ApiResponse<Boolean> updateColorsById(@RequestBody ColorSettingParams params){
        return ApiResponse.success(service.updateColorsById(params.getId(),params.listColors()));
    }
    @RequestMapping(value="/deleteColorSeries",method=RequestMethod.GET,produces="application/json;charset=UTF-8")
    public ApiResponse<Boolean> deleteColorSeries(@RequestParam("id")long id){
        return ApiResponse.success(service.deleteColorSeries(id));
    }
    @RequestMapping(value="/colorBar",method=RequestMethod.GET,produces="application/json;charset=UTF-8")
    public ApiResponse<List<ColorBar>> listColorSeriesByTime(
            @RequestParam(value = "start")String start
            ,@RequestParam(value = "end")String end){
        return ApiResponse.success(service.listColorBar(start,end));
    }

    @ApiOperation(value = "根据报汛站等级获取站点信息", notes = "根据报汛站等级(逗号分割)获取站点信息")
    @RequestMapping(value = "listRainStationByFrgrds", method = RequestMethod.GET)
    public ApiResponse<List<RainStation>> listRainStationByFrgrds(@ApiParam(example = "1,2",value = "报汛站等级")
                                                                      @RequestParam String frgrds){
        return ApiResponse.success(rainService.listRainStationByFrgrd(frgrds));
    }
}
