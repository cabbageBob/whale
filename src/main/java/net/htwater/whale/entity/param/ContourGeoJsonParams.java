package net.htwater.whale.entity.param;

import cn.hutool.core.util.HexUtil;
import com.google.common.base.Strings;
import lombok.Data;
import net.htwater.whale.entity.DiscretePoint;
import org.meteoinfo.data.StationData;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * draw contour param
 * @author  shitianlong
 * @
 */
@Data
public class ContourGeoJsonParams {
    public static final String COLON=":";
    public static final String COMMA=",";

    private String breaks;
    private String colors;
    private String stations;
    private int type;
    private String district;

    public StationData getStationData(){
        StationData result=new StationData();
        String[] data=this.stations.split(COMMA);
        int i=0;
        for(String s:data){
            String[] datum=s.split(COLON);
            result.addData(String.valueOf(i),Double.valueOf(datum[0]),Double.valueOf(datum[1]),Double.valueOf(datum[2]));
            i++;
        }
        return result;
    }
    public double[] getBreakArray(){
        String[] breakString=this.breaks.split(COMMA);
        double[] result=new double[breakString.length];
        for (int i = 0; i <breakString.length ; i++) {
            result[i]=Double.valueOf(breakString[i]);
        }
        return result;
    }
    public Color[] getColorArray(){
        String[] colorString=this.colors.split(COMMA);
        Color[] result=new Color[colorString.length];
        for (int i = 0; i <colorString.length ; i++) {
            result[i]=HexUtil.decodeColor(colorString[i]);
        }
        return result;
    }
}
