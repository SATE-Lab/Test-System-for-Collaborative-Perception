package com.nineya.springboot.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author cw
 * @since 2023-04-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("pointcloud")
public class Pointcloud implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField("name")
    private String name;

    @TableField("size")
    private Integer size;

    @TableField("fileType")
    private Integer filetype;

    @TableField("address")
    private String address;

    @TableField("uploadTime")
    private Date uploadtime;

    @TableField("sence")
    private Integer sence;

    @TableField("car")
    private Integer car;


}
