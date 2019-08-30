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
 * 预约看房信息表
 * </p>
 *
 * @author xiadong
 * @since 2019-08-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("house_subscribe")
public class HouseSubscribe implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 房源id
     */
    private Integer houseId;
    /**
     * 用户id
     */
    private Integer userId;
    /**
     * 用户描述
     */
    private String desc;
    /**
     * 预约状态 1-加入待看清单 2-已预约看房时间 3-看房完成
     */
    private Integer status;
    /**
     * 数据创建时间
     */
    private Date createTime;
    /**
     * 记录更新时间
     */
    private Date lastUpdateTime;
    /**
     * 预约时间
     */
    private Date orderTime;
    /**
     * 联系电话
     */
    private String telephone;
    /**
     * 房源发布者id
     */
    private Integer adminId;


}
