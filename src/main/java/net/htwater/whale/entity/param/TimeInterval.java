package net.htwater.whale.entity.param;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

/**
 * time interval
 * @author Jokki
 */
@Data
@AllArgsConstructor
public class TimeInterval {
    private Date tm1;
    private Date tm2;
}
