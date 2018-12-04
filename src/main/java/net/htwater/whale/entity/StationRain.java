package net.htwater.whale.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author shitianlong
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StationRain {
    private String stcd;
    private String stnm;
    private double lgtd;
    private double lttd;
    private double value;
    public DiscretePoint convertToPoint(){
        return new DiscretePoint(this.getStcd(),this.getLgtd(),this.getLttd(),this.getValue());
    }
}
