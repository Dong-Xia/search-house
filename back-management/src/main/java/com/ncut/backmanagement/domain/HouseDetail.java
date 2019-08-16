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
 * 
 * </p>
 *
 * @author xiadong
 * @since 2019-08-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("house_detail")
public class HouseDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 详细描述
     */
    private String description;
    /**
     * 户型介绍
     */
    private String layoutDesc;
    /**
     * 交通出行
     */
    private String traffic;
    /**
     * 周边配套
     */
    private String roundService;
    /**
     * 租赁方式
     */
    private Integer rentWay;
    /**
     * 详细地址 
     */
    private String address;
    /**
     * 附近地铁线id
     */
    private Integer subwayLineId;
    /**
     * 附近地铁线名称
     */
    private String subwayLineName;
    /**
     * 地铁站id
     */
    private Integer subwayStationId;
    /**
     * 地铁站名
     */
    private String subwayStationName;
    /**
     * 对应house的id
     */
    private Integer houseId;


}
