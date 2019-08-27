package com.ncut.backmanagement.domain;

import lombok.Data;

/**
 * <b>System：</b>ncc<br/>
 * <b>Title：</b>HouseSearch<br/>
 * <b>Description：</b>接收搜索条件<br/>
 * <b>@author： </b>xiadong<br/>
 * <b>@date：</b>2019/8/22 23:35<br/>
 */
@Data
public class HouseSearch {
    private String keyWord;
    private String cityEnName;
    private String regionEnName;
    private String priceBlock;
    private String areaBlock;
    private int room;
    private int direction;
    private int rentWay = -1;
    private String orderBy = "lastUpdateTime";
    private String orderDirection = "desc";
}
