package com.nineya.springboot.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
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
@TableName("report")
public class Report implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "ID", type = IdType.AUTO)
    private Integer id;

    @TableField("name")
    private String name;

    @TableField("image")
    private String image;

    @TableField("alarm")
    private String alarm;

    @TableField("miss")
    private String miss;

    @TableField("yaw")
    private String yaw;

    @TableField("bev")
    private String bev;

    @TableField("augId")
    private Integer augid;

    @TableField("testId")
    private Integer testid;

    @TableField("testInfo")
    private String testInfo;


}
