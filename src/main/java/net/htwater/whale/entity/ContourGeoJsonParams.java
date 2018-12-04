package net.htwater.whale.entity;

import lombok.Data;

/**
 * @author: Jokki
 * @description:
 * @date: Created in 上午10:21 18-9-11
 * @modified By:
 */
@Data
public class ContourGeoJsonParams {

    private String start;

    private String end;

    private String breaks;

    private String stations;

    private int type;

    private String district;
}
