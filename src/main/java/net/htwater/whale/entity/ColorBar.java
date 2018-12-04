package net.htwater.whale.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author shitianlong
 * @since 2018-09-21
 */
@Data
@AllArgsConstructor
public class ColorBar {
    private long id;
    private String name;
    private String[] colors;
}
