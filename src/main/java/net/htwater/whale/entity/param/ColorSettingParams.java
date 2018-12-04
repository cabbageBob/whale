package net.htwater.whale.entity.param;

import lombok.Data;
import net.htwater.whale.entity.Color;
import net.htwater.whale.entity.ColorSeries;

import java.util.ArrayList;
import java.util.List;

/**
 * @author shitianlong
 */
@Data
public class ColorSettingParams {
    public static final String COMMA=",";
    private long id;
    private String colors;
    private String name;
    private boolean isDefault;

    public List<Color> listColors(){
        if(colors.contains(COMMA)) {
            String[] color = colors.split(",");
            List<Color> colors=new ArrayList<>();
            for(String s:color){
                Color color1=new Color();
                color1.setColor(s);
                colors.add(color1);
            }
            return colors;
        }else{
            return null;
        }
    }
    public ColorSeries transformToSeries(){
        ColorSeries series=new ColorSeries();
        series.setId(id);
        series.setIsDefault(isDefault?1:0);
        series.setName(name);
        return series;
    }
}
