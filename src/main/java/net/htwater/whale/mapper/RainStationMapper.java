package net.htwater.whale.mapper;

import net.htwater.whale.entity.RainStation;
import net.htwater.whale.entity.StationRain;
import org.apache.ibatis.annotations.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author shitianlong
 */
@Mapper
public interface RainStationMapper {
    /**
     * query station info and rain info for interpolation
     * @param start start time
     * @param end end time
     * @param include stations
     * @return list
     */
    @Select(" select a.stcd,a.stnm,a.lgtd,a.lttd, b.value from st_stbprp_b a, " +
            " (select sum(DRP) value ,stcd from st_pptn_r where TM > #{start} and  TM <= #{end} and stcd  in (${include}) group by stcd) b " +
            " where a.stcd=b.stcd order by b.value desc")
    public List<StationRain> getStationRainByTime(@Param("start") String start, @Param("end") String end
            ,@Param("include")String include);

    /**
     * query all station info and rain info for interpolation
     * @param start time
     * @param end time
     * @return list
     */
    @Select(" select a.stcd,a.stnm,a.lgtd,a.lttd, b.value from st_stbprp_b a, " +
            " (select sum(DRP) value ,stcd from st_pptn_r where TM > #{start} and  TM <= #{end} group by stcd) b " +
            " where a.stcd=b.stcd and b.value>0 order by b.value desc")
    public List<StationRain> getAllStationRainByTime(@Param("start") String start, @Param("end") String end);

    /**
     * list station with specified information
     * @return list
     */
    @Select("select stcd,stnm,lgtd,lttd,dtmel,esstym,locality from st_stbprp_b order by stcd")
    public List<RainStation> listRainStation();

    /**
     * get region rain
     * @param tm1 start time
     * @param tm2 end time
     * @return int
     */
    @Insert("replace into summary_region_day_rain select '330000',#{tm1},sum(drp)/count(*) value from(select a.stcd, SUM(DRP) drp from st_pptn_r a left join st_stbprp_b b " +
            "on a.stcd=b.stcd  where ITEM like '%P%' and tm>=#{tm1} and tm<#{tm2} group by stcd)a")
    int getRegionRain(@Param("tm1")Date tm1, @Param("tm2") Date tm2);

    /**
     * get max time
     * @return map
     */
    @Select("select max(tm) maxTm, min(tm) minTm from st_pptn_r")
    Map<String, Date> getMaxTm();

    /**
     * select last time
     * @return map
     */
    @Select("select last_time calTm from summary_calculate_time")
    Map<String, Date> getLastCalculateTm();

    /**
     * update time
     * @param tm time
     * @return int
     */
    @Update("update summary_calculate_time set last_time=#{tm}")
    int updateCalculateTm(@Param("tm") Date tm);

    /**
     * frgrds
     * @param frgrds 防汛等级
     * @return list
     */
    @Select("select stcd,stnm,lgtd,lttd,dtmel,esstym,locality from st_stbprp_b where FRGRD in (${frgrds}) order by stcd ")
    List<RainStation> listRainStationByFrgrd(@Param("frgrds") String frgrds);
}
