package com.ncut.backmanagement.service;


import com.baomidou.mybatisplus.service.IService;
import com.ncut.backmanagement.common.ServiceMultiResult;
import com.ncut.backmanagement.domain.House;
import com.ncut.backmanagement.domain.HouseDetail;
import com.ncut.backmanagement.domain.HouseIndexTemplate;
import com.ncut.backmanagement.domain.RentSearch;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 房屋信息表 服务类
 * </p>
 *
 * @author xiadong
 * @since 2019-08-15
 */
public interface IHouseService extends IService<House> {

    /**
     * 批量同步数据到es中
     * @param houses
     */
    void addBulkDataToEs(List<House> houses, Map<Integer, HouseDetail> houseDetailMap);

    /**
     * 上架一个新商品时将数据同步到es中
     */
    void addSingleNewDataToEs(String data, int houseId);


    /**
     * 更新商品信息到es中
     * @param s
     * @param houseId
     */
    void updateSingleDataToEs(HouseIndexTemplate s, int houseId);

    /**
     *
     * 删除es中指定商品
     * @param houseId
     */
    void deleteSingleDataFromEs(int houseId);

    /**
     * 搜索引擎查询数据
     */
    ServiceMultiResult<Integer> searchData(RentSearch rentSearch);
}
