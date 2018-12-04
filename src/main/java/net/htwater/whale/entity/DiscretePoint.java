package net.htwater.whale.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

/**
 * @author shitianlong
 * @since 2018-09-04
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DiscretePoint implements Serializable {
    private String stcd;
    private double lgtd;
    private double lttd;
    private double value;

}
