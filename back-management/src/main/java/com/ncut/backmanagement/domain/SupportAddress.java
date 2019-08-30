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
 * @since 2019-08-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("support_address")
public class SupportAddress implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 上一级行政单位名
     */
    private String belongTo;
    /**
     * 行政单位英文名缩写
     */
    private String enName;
    /**
     * 行政单位中文名
     */
    private String cnName;
    /**
     * 行政级别 市-city 地区-region
     */
    private String level;
    /**
     * 百度地图经度
     */
    private Double baiduMapLng;
    /**
     * 百度地图纬度
     */
    private Double baiduMapLat;
    /**
     * 行政级别定义
     */
    public enum Level {
        CITY("city"),
        REGION("region");

        private String value;

        Level(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public static Level of(String value) {
            for (Level level : Level.values()) {
                if (level.getValue().equals(value)) {
                    return level;
                }
            }

            throw new IllegalArgumentException();
        }
    }

}
