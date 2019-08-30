package com.ncut.backmanagement.service;

import com.ncut.backmanagement.common.ServiceMultiResult;
import com.ncut.backmanagement.common.VO.HouseDTO;
import com.ncut.backmanagement.domain.RentSearch;


/**
 * <b>System：</b>ncc<br/>
 * <b>Title：</b>SearchDataService<br/>
 * <b>Description：</b><br/>
 * <b>@author： </b>xiadong<br/>
 * <b>@date：</b>2019/8/22 22:54<br/>
 */
public interface SearchDataService {

    // 根据关键词搜索数据
    ServiceMultiResult<HouseDTO> searchData(RentSearch houseSearch);
}
