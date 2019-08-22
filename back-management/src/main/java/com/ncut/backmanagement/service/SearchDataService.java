package com.ncut.backmanagement.service;

import com.ncut.backmanagement.domain.House;
import com.ncut.backmanagement.domain.HouseSearch;

import java.util.List;

/**
 * <b>System：</b>ncc<br/>
 * <b>Title：</b>SearchDataService<br/>
 * <b>Description：</b><br/>
 * <b>@author： </b>xiadong<br/>
 * <b>@date：</b>2019/8/22 22:54<br/>
 */
public interface SearchDataService {

    // 根据关键词搜索数据
    List<House> searchData(HouseSearch SearchKeyWord);
}
