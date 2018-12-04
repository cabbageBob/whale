package net.htwater.whale.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author shitianlong
 * @since 2018-9-5
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Interval {
    private double max;
    private double min;
    private String color;
}
