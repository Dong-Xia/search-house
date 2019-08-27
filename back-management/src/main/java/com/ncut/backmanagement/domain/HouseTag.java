package com.ncut.backmanagement.domain;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotations.Version;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 房屋标签映射关系表
 * </p>
 *
 * @author xiadong
 * @since 2019-08-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("house_tag")
public class HouseTag implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 房屋id
     */
    private Integer houseId;
    /**
     * 标签id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private String name;


}
