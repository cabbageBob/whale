package net.htwater.whale.mapper;

import net.htwater.whale.entity.StationSeries;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 *
 * @author shitianlong
 * @since 2018-09-06
 */
@Mapper
public interface StationSeriesMapper extends BaseMapper<StationSeries> {
    /**
     * make all item isn't default choice
     * @return
     */
    @Update("update contour_station_series set is_default=0")
    public  boolean clearDefaultSeries();

    /**
     * set default color series choice
     * @param id
     * @return
     */
    @Update("update contour_station_series set is_default=1 where id=#{id}")
    public  boolean setDefaultSeries(@Param("id") long id);

    /**
     * count
     * @return
     */
    @Update("select count(*) from  contour_station_series")
    public  long countSeries();

}
