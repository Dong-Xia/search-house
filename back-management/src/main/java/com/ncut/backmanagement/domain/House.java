package com.ncut.backmanagement.domain;

import com.baomidou.mybatisplus.enums.IdType;
import java.util.Date;
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
 * 房屋信息表
 * </p>
 *
 * @author xiadong
 * @since 2019-08-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("house")
public class House implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * house唯一标识
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private String title;
    /**
     * 价格
     */
    private Integer price;
    /**
     * 面积
     */
    private Integer area;
    /**
     * 卧室数量
     */
    private Integer room;
    /**
     * 楼层
     */
    private Integer floor;
    /**
     * 总楼层
     */
    private Integer totalFloor;
    /**
     * 被看次数
     */
    private Integer watchTimes;
    /**
     * 建立年限
     */
    private Integer buildYear;
    /**
     * 房屋状态 0-未审核 1-审核通过 2-已出租 3-逻辑删除
     */
    private Integer status;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 最近数据更新时间
     */
    private Date lastUpdateTime;
    /**
     * 城市标记缩写 如 北京bj
     */
    private String cityEnName;
    /**
     * 地区英文简写 如昌平区 cpq
     */
    private String regionEnName;
    /**
     * 封面
     */
    private String cover;
    /**
     * 房屋朝向
     */
    private Integer direction;
    /**
     * 距地铁距离 默认-1 附近无地铁
     */
    private Integer distanceToSubway;
    /**
     * 客厅数量
     */
    private Integer parlour;
    /**
     * 所在小区
     */
    private String district;
    /**
     * 所属管理员id
     */
    private Integer adminId;
    private Integer bathroom;
    /**
     * 街道
     */
    private String street;


}
