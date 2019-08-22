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
    private String price;
    private String keyWord;
}
