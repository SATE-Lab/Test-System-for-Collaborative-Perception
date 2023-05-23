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
 * @since 2023-04-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("testtask")
public class Testtask implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "ID", type = IdType.AUTO)
    private Integer id;

    @TableField("testSetId")
    private String testsetid;

    @TableField("errorSetId")
    private String errorsetid;

    @TableField("augTaskId")
    private Integer augtaskid;

    @TableField("createTime")
    private Date createtime;

    @TableField("status")
    private Integer status;

    @TableField("name")
    private String name;


}
