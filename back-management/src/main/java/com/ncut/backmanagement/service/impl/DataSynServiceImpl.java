package com.ncut.backmanagement.service.impl;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import com.ncut.backmanagement.dao.HouseDetailMapper;
import com.ncut.backmanagement.dao.HouseMapper;
import com.ncut.backmanagement.domain.House;
import com.ncut.backmanagement.domain.HouseDetail;
import com.ncut.backmanagement.domain.HouseIndexTemplate;
import com.ncut.backmanagement.service.DataSynService;
import com.ncut.backmanagement.service.IHouseService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <b>System：</b>ncc<br/>
 * <b>Title：</b>DataSynServiceImpl<br/>
 * <b>Description：</b>数据同步服务实现类<br/>
 * <b>@author： </b>xiadong<br/>
 * <b>@date：</b>2019/8/15 16:27<br/>
 */
@Service
public class DataSynServiceImpl implements DataSynService {
    @Autowired
    private HouseDetailMapper houseDetailMapper;

    @Autowired
    private HouseMapper houseMapper;

    @Autowired
    private IHouseService iHouseService;

    @Override
    public void synData() {
        // 1、获取数据库中的数据
        List<House> houses = houseMapper.selectList(null);
        List<HouseDetail> houseDetails = houseDetailMapper.selectList(null);
        Map<Integer, HouseDetail> houseDetailMap = new HashMap<>();
        houseDetails.forEach(houseDetail -> {
            houseDetailMap.put(houseDetail.getHouseId(), houseDetail);
        });
        // 2、同步数据到es搜索引擎中
        iHouseService.addBulkDataToEs(houses,houseDetailMap);

    }

    @Override
    public void addSingleNewDataToEs(int houseId) {
        // 1、获取数据库中的数据
        House house = houseMapper.selectById(houseId);
        HouseDetail houseDetail = houseDetailMapper.selectOne(new HouseDetail().setHouseId(houseId));
        HouseIndexTemplate houseIndexTemplate = new HouseIndexTemplate();
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.map(house,houseIndexTemplate);
        modelMapper.map(houseDetail,houseIndexTemplate);
        JSON parse = JSONUtil.parse(houseIndexTemplate);
        iHouseService.addSingleNewDataToEs(parse.toString(),houseId);
    }

    @Override
    public void updateSingleDataToEs(int houseId) {
        House house = houseMapper.selectById(houseId);
        HouseDetail houseDetail = houseDetailMapper.selectOne(new HouseDetail().setHouseId(houseId));
        HouseIndexTemplate houseIndexTemplate = new HouseIndexTemplate();
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.map(house,houseIndexTemplate);
        modelMapper.map(houseDetail,houseIndexTemplate);
        iHouseService.updateSingleDataToEs(houseIndexTemplate,houseId);
    }

    @Override
    public void deleteSingleDataFromEs(int houseId) {
        iHouseService.deleteSingleDataFromEs(houseId);
    }
}
