package net.htwater.whale.entity;

import com.baomidou.mybatisplus.enums.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;


import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author shitianlong
 * @since 2018-09-21
 */
@Data
@Accessors(chain = true)
@TableName("contour_fixed_intervals")
public class FixedIntervals implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    @TableField("type")
    private Integer type;
    @TableField("min")
    private Double min;
    @TableField("max")
    private Double max;
    @TableField("color")
    private String color;
    @TableField("gmt_create")
    private Date gmtCreate;
    @TableField("gmt_modified")
    private Date gmtModified;


}
