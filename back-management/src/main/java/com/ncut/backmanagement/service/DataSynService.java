package com.ncut.backmanagement.service;

/**
 * <b>System：</b>ncc<br/>
 * <b>Title：</b>DataSynService<br/>
 * <b>Description：</b>数据库同步到es中服务类<br/>
 * <b>@author： </b>xiadong<br/>
 * <b>@date：</b>2019/8/16 10:47<br/>
 */
public interface DataSynService {
    /**
     * 同步数据库数据到es中
     */
    void synData();

    /**
     * 上架一个新商品时将数据同步到es中
     */
    void addSingleNewDataToEs(int houseId);

    /**
     * 商品更新数据到es中
     */
    void updateSingleDataToEs(int houseId);

    /**
     * 删除商品时将数据从es中删除
     */
    void deleteSingleDataFromEs(int houseId);
}
