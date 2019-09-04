package com.ncut.backmanagement.service;

import com.ncut.backmanagement.common.ServiceMultiResult;
import com.ncut.backmanagement.common.ServiceResult;
import com.ncut.backmanagement.common.VO.HouseDTO;
import com.ncut.backmanagement.domain.RentSearch;

import java.util.List;


/**
 * <b>System：</b>ncc<br/>
 * <b>Title：</b>SearchDataService<br/>
 * <b>Description：</b><br/>
 * <b>@author： </b>xiadong<br/>
 * <b>@date：</b>2019/8/22 22:54<br/>
 */
public interface SearchDataService {

    /**
     * 根据关键词搜索数据
     * @param houseSearch
     * @return
     */
    ServiceMultiResult<HouseDTO> searchData(RentSearch houseSearch);

    /**
     * 获取补全建议关键词
     */
    ServiceResult<List<String>> suggest(String prefix);
}
