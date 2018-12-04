package net.htwater.whale.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author  shitianlong
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RainStation {
    private String stcd;
    private String stnm;
    private double lgtd;
    private double lttd;
    private double dtmel;
    private String esstym;
    private String locality;
    private boolean selected;
    public boolean getSelected(){
        return selected;
    }
}
