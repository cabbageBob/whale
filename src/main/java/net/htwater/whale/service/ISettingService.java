package net.htwater.whale.service;

import net.htwater.whale.entity.*;

import java.util.List;

/**
 * @author shitianlong
 * @since 2018-09-04
 */
public interface ISettingService {

     /**
      * list all color series
      * @author shitianlong
      * @since 2018-09-04
      * @return List<ColorSeries>
      */
     List<ColorSeries> listColorSeries();
     /**
      * add color series
      * @param series series information
      * @param colors colors in this series
      * @author shitianlong
      * @since 2018-09-04
      * @return long
      */
     long addColorSeries(ColorSeries series, List<Color>  colors);

     /**
      * update series information
      * @author shitianlong
      * @since 2018-09-04
      * @param series bean
      * @return boolean
      */
     boolean updateColorSeries(ColorSeries series);


     /**
      * update  color
      * @author shitianlong
      * @since 2018-09-04
      * @param id id
      * @param colors colors
      * @return boolean
      */
     boolean updateColorsById(long id,List<Color> colors);

     /**
      * delete a color series
      * @author shitianlong
      * @since 2018-09-04
      * @param colorSeriesId id of color series
      * @return boolean
      */
     boolean deleteColorSeries(Long colorSeriesId);


     /**
      * get all color of a series
      * @author shitianlong
      * @since 2018-09-04
      * @param colorSeriesId id of color series
      * @return List<Color>
      */
     List<Color> listColorsBySeriesId(Long colorSeriesId);

     /**
      * add series station
      * @author shitianlong
      * @since 2018-09-04
      * @param series bean
      * @param stations List<Station>
      * @return long
      */
     long addStationSeries(StationSeries series, List<Station> stations);


     /**
      * update series
      * @author shitianlong
      * @since 2018-09-04
      * @param series bean
      * @return boolean
      */
     boolean updateStationSeries(StationSeries series);


     /**
      * update series
      * @author shitianlong
      * @since 2018-09-04
      * @param id id
      * @param stations List<Station>
      * @return boolean
      */
     boolean updateStation (long id,List<Station> stations);

     /**
      * delete series
      * @author shitianlong
      * @since 2018-09-04
      * @param seriesId id of series
      * @return boolean
      */
     boolean deleteStationSeries(Long seriesId);



     /**
      * get all series for list
      * @author shitianlong
      * @since 2018-09-04
      * @return List<StationSeries>
      */
     List<StationSeries> listStationSeries();

     /**
      * get all station in a series
      * @author shitianlong
      * @since 2018-09-04
      * @param seriesId station series
      * @return List<RainStation>
      */
     List<Station> listStations(Long seriesId);
     /**
      * list station in standard table for vacuate
      * @author shitianlong
      * @since 2018-09-04
      * @param  kilometres distance
      * @return List<RainStation>
      */
     List<RainStation> listStationForVacuate(int kilometres);

     /**
      * list color information for color pick and visualization
      * @param start start time
      * @param end end time
      * @return List
      */
     List<ColorBar> listColorBar(String start,String end);

     /**
      * get fixed meteorology intervals
      * @param type type
      * @return list @see FixedIntervals
      */
     List<FixedIntervals> listFixedIntervals(int type);
}
