package com.ncut.backmanagement.domain;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * <b>System：</b>ncc<br/>
 * <b>Title：</b>HouseIndexTemplate<br/>
 * <b>Description：</b>同步数据到es中：索引结构模板<br/>
 * <b>@author： </b>xiadong<br/>
 * <b>@date：</b>2019/8/16 15:21<br/>
 */
@Data
public class HouseIndexTemplate {
        private Long houseId;

        private String title;

        private int price;

        private int area;

        private Date createTime;

        private Date lastUpdateTime;

        private String cityEnName;

        private String regionEnName;

        private int direction;

        private int distanceToSubway;

        private String subwayLineName;

        private String subwayStationName;

        private String street;

        private String district;

        private String description;

        private String layoutDesc;

        private String traffic;

        private String roundService;

        private int rentWay;

        private List<String> tags;
        /**
         * 卧室数量
         */
        private Integer room;
        /**
         * 总楼层
         */
        private Integer totalFloor;
        /**
         * 建立年限
         */
        private Integer buildYear;
        /**
         * 楼层
         */
        private Integer floor;
        /**
         * 房屋状态 0-未审核 1-审核通过 2-已出租 3-逻辑删除
         */
        private Integer status;

        private Integer bathroom;

        /**
         * 客厅数量
         */
        private Integer parlour;

/*        private List<HouseSuggest> suggest;

        private BaiduMapLocation location;*/
    }
