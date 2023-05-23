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
 * @since 2023-05-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("visual")
public class Visual implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "ID", type = IdType.AUTO)
    private Integer id;

    @TableField("address")
    private String address;

    @TableField("type")
    private Integer type;

    @TableField("augId")
    private Integer augid;


}
