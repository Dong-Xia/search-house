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
 * 房屋图片信息
 * </p>
 *
 * @author xiadong
 * @since 2019-08-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("house_picture")
public class HousePicture implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 所属房屋id
     */
    private Integer houseId;
    /**
     * 图片路径
     */
    private String cdnPrefix;
    /**
     * 宽
     */
    private Integer width;
    /**
     * 高
     */
    private Integer height;
    /**
     * 所属房屋位置
     */
    private String location;
    /**
     * 文件名
     */
    private String path;


}
