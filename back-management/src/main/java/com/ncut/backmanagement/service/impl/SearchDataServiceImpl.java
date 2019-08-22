package com.ncut.backmanagement.service.impl;

import com.ncut.backmanagement.domain.House;
import com.ncut.backmanagement.domain.HouseSearch;
import com.ncut.backmanagement.service.SearchDataService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <b>System：</b>ncc<br/>
 * <b>Title：</b>SearchDataServiceImpl<br/>
 * <b>Description：</b><br/>
 * <b>@author： </b>xiadong<br/>
 * <b>@date：</b>2019/8/22 23:00<br/>
 */
@Service
public class SearchDataServiceImpl implements SearchDataService {
    /**
     * 该方法实现需结合elasticsearch 和 mysql
     * @param SearchKeyWord
     * @return
     */
    @Override
    public List<House> searchData(HouseSearch SearchKeyWord) {
        // 1、当用户没有输入和选择搜索相关的功能时，则不走搜索引擎，直接查询数据库，进行数据的返回

        // 2、当选择了搜索的功能，则先走搜索引擎，在搜索引擎中查到id后，再根据id去数据库中查询数据返回

        return null;
    }
}
