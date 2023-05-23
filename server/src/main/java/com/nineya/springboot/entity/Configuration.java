package com.nineya.springboot.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author ylq
 * @since 2023-04-02
 */
@TableName("configuration")
public class Configuration implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("id")
    private Integer id;

    @TableField("name")
    private String name;

    @TableField("size")
    private Double size;

    @TableField("create_date")
    private Date createDate;

    @TableField("type")
    private String type;

    @TableField("desc")
    private String desc;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public Double getSize() {
        return size;
    }

    public void setSize(Double size) {
        this.size = size;
    }
    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return "Configuration{" +
            "id=" + id +
            ", name=" + name +
            ", size=" + size +
            ", createDate=" + createDate +
            ", type=" + type +
            ", desc=" + desc +
        "}";
    }
}
