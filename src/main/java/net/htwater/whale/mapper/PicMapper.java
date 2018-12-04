package net.htwater.whale.mapper;

import net.htwater.whale.entity.Picture;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @author wangzhfan
 */
@Mapper
public interface PicMapper {
    /**保存图片信息
     *
     * @param name 图片名称
     * @param path 路径
     * @param time 时间
     * @param type 间隔类型
     */
    @Update("insert into contour_pic (pic_name,path,generated_time,type) values(#{name},#{path},#{time},#{type})")
    public void savePicInfo(@Param("name") String name, @Param("path") String path ,
                            @Param("time") String time, @Param("type") String type);

    /**
     * 获取日等值面图片信息
     * @param type 类型
     * @param start 开始时间
     * @param end 结束时间
     * @return list
     */
    @Select("select * from contour_pic where type = #{type} and generated_time >= #{start} " +
            "and generated_time <= #{end} ORDER BY generated_time  ")
    public List<Picture> getPicByTimeAndType(@Param("type") String type,@Param("start") String start,@Param("end") String end);

    /**
     * 按照类型获取最新的等值面图片
     * @param type duration
     * @return  Picture
     */
    @Select("select * from contour_pic where type=#{type} ORDER BY generated_time desc limit 1")
    public Picture getTodayPictureByType(@Param("type") String type);
    /**
     * 按照类型获取最新的等值面图片
     * @param type duration
     * @param date example "2018-09-10"
     * @return  Picture
     */
    @Select("select * from contour_pic where type=#{type} and generated_time=#{date} limit 1")
    public Picture getPictureByTypeAndDate(@Param("type") String type,@Param("date") String date);
}
