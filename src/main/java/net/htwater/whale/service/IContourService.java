package net.htwater.whale.service;

import net.htwater.whale.entity.Interval;
import net.htwater.whale.entity.Picture;
import net.htwater.whale.entity.StationRain;
import org.meteoinfo.data.StationData;

import java.awt.*;
import java.util.List;
/**
 * @author shitianlong
 * @since 2018-09-04
 */
public interface IContourService {
    /**
     * draw contour feature by idw method
     * @param data input data
     * @param breaks interval values
     * @param district district code
     * @param colors  array of colors
     * @param type 1|2|3
     * @param duration @see DateTimeUtil
     * @return <p>@see contourUtil.drawContourFeature(args)</p>
     */
    String drawContourFeature(StationData data, double[] breaks, String district, Color[] colors, int type,int duration);

    /**
     * get contour levels by computer recommendation
     * @param min "YY-MM-DD hh:mm:ss"
     * @param max "YY-MM-DD hh:mm:ss"
     * @param colorBand int the id of certain band
     * @return List<Interval>
     */
    List<Interval> listContourLevelsByExtreme(double min, double max, Long colorBand);
    /**
     *get stations for contour
     * @param startTime "YY-MM-DD hh:mm:ss"
     * @param endTime   "YY-MM-DD hh:mm:ss"
     * @param include
     * 1:high resolution 2:medium resolution 3:low resolution 4：national station
     * @return List<Interval>
     */
    List<StationRain> listContourStations(String startTime, String endTime, String include);

    /**
     * get picture by type and time
     * @param type  rain duration type
     * @param begin begin time
     * @param end end time
     * @return list
     */
    List<Picture> getContourPicturePath(String type,String begin,String end);

    /**
     * save picture info
     * @param pictureName generate picture name
     * @param path file path
     * @param time generateTime
     * @param type rain duration type
     */
     void addPicture(String pictureName,String path,String time,String type);

    /**
     * get station recommend by criterion
     * @param start time to start
     * @param end end time
     * @return list
     */
     List<StationRain> listAllRainStation(String start,String end);

    /**
     *get contour picture by type
     * @param type duration
     * @return picture
     */
     Picture getLatestPictureByType(String type);

    /**
     * get contour picture by date and type
     * @param type duration
     * @param date date
     * @return picture
     */
     Picture getPictureByTypeAndDate(String type,String date);
}
