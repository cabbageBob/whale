package net.htwater.whale.entity;

import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author: Jokki
 * @Description:
 * @Date: Created in 15:20.2018/9/17
 * @Modified By:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("summary_region_day_rain")
public class RegionRain {
    private String addvcd;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date tm;

    private BigDecimal drp;

}
