package net.htwater.whale.entity.param;

import lombok.Data;
import net.htwater.whale.entity.Station;
import net.htwater.whale.entity.StationSeries;

import java.util.ArrayList;
import java.util.List;

/**
 * @author  shitianlong
 */
@Data
public class StationSettingParams {
    public static final String COMMA=",";
    private long id;
    private String name;
    private String stations;
    private boolean choose;

    public List<Station> listStations(){
        if(!stations.contains(COMMA)){
            return null;
        }
        List<Station> result=new ArrayList<>();
        String[] stationCode=stations.split(",");
        for (String  s:stationCode){
            Station station=new Station();
            station.setStcd(s);
            result.add(station);
        }
        return result;
    }
    public StationSeries transformToStationSeries(){
        StationSeries series=new StationSeries();
        series.setName(name);
        series.setId(id);
        series.setIsDefault(choose?1:0);
        return series;
    }
}
