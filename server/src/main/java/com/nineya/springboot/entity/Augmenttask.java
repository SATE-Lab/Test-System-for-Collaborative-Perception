package com.nineya.springboot.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
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
 * @since 2023-05-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("augmenttask")
public class Augmenttask implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField("originSetId")
    private String originsetid;

    @TableField("augSetId")
    private String augsetid;

    @TableField("weatherType")
    private Integer weathertype;

    @TableField("weatherIntensity")
    private Double weatherintensity;

    @TableField("parameter")
    private Double parameter;

    @TableField("beginTime")
    private Date begintime;

    @TableField("status")
    private Integer status;

    @TableField("taskname")
    private String taskname;

    @TableId(value = "ID", type = IdType.AUTO)
    private Integer id;

    @TableField("road")
    private Integer road;


}
