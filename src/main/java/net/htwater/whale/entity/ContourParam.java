package net.htwater.whale.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;
/**
 * @author shitianlong
 * @since 2018-09-04
 */
@Data
public class ContourParam {
    @ApiModelProperty(value="离散点集合",required = true)
    @NotNull(message="points不能为空")
    private List<DiscretePoint> points;
    @ApiModelProperty(value="插值基准",required = true)
    @NotNull(message="base不能为空")
    private int base;
    @ApiModelProperty(value="插值间隔",required = true)
    @NotNull(message="interval不能为空")
    private int interval;
    @ApiModelProperty(value="插值等级",required = true)
    @NotNull(message="level不能为空")
    private int level;
}
