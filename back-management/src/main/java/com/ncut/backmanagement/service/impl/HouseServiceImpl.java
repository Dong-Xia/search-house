package com.ncut.backmanagement.service.impl;

import com.ncut.backmanagement.dao.HouseMapper;
import com.ncut.backmanagement.domain.House;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ncut.backmanagement.domain.HouseDetail;
import com.ncut.backmanagement.service.IHouseService;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 房屋信息表 服务实现类
 * </p>
 *
 * @author xiadong
 * @since 2019-08-15
 */
@Service
public class HouseServiceImpl extends ServiceImpl<HouseMapper, House> implements IHouseService {
    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Override
    public void addBulkDataToEs(List<House> houses,Map<Integer, HouseDetail> houseDetailMap) {
        // es批量操作类
        BulkRequest bulkRequest = new BulkRequest();
        // 只对常用于搜索的数据进行同步到es中，因为es进行搜索的时候会将数据从磁盘加载到内存中，内存空间有限
        houses.forEach(house -> {
            HouseDetail houseDetail = houseDetailMap.get(house.getId());
            IndexRequest indexRequest = new IndexRequest();
            // 指定es索引、类型
            indexRequest.index("search_house").type("house").id(house.getId().toString());
            // 构建存入es的json文档
            Map<String, Object> map = new HashMap<>();
            map.put("title", house.getTitle());
            map.put("price", house.getPrice());
            map.put("area", house.getArea());
            map.put("room", house.getRoom());
            map.put("floor", house.getFloor());
            map.put("totalFloor", house.getTotalFloor());
            map.put("buildYear", house.getBuildYear());
            map.put("status", house.getStatus());
            map.put("cityEnName", house.getCityEnName());
            map.put("regionEnName", house.getRegionEnName());
            map.put("direction", house.getDirection());
            map.put("parlour", house.getParlour());
            map.put("district", house.getDistrict());
            map.put("bathroom", house.getBathroom());
            map.put("street", house.getStreet());
            map.put("distanceToSubway", house.getDistanceToSubway());
            map.put("subwayLineName", houseDetail.getSubwayLineName());
            map.put("subwayStationName", houseDetail.getSubwayStationName());
            map.put("createTime", house.getCreateTime());
            map.put("lastUpdateTime", house.getLastUpdateTime());
            map.put("description", houseDetail.getDescription());
            map.put("layoutDesc", houseDetail.getLayoutDesc());
            map.put("traffic", houseDetail.getTraffic());
            map.put("rentWay", houseDetail.getRentWay());
            indexRequest.source(map);
            bulkRequest.add(indexRequest);
        });
        try {
            restHighLevelClient.bulk(bulkRequest);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addSingleNewDataToEs(String data, int houseId) {
        IndexRequest indexRequest = new IndexRequest();
        indexRequest.index("search_house").type("house").id(String.valueOf(houseId));
        // 以字符串形式提供的文档源，区别于上面使用map形式，这种更加简洁
        indexRequest.source(data, XContentType.JSON);

        try {
            restHighLevelClient.index(indexRequest);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateSingleDataToEs(String data, int houseId) {
        UpdateRequest updateRequest = new UpdateRequest();
        updateRequest.index("search_house").type("house").id(String.valueOf(houseId));
        updateRequest.doc(data, XContentType.JSON); //以JSON格式的字符串形式提供的部分文档源

        try {
            restHighLevelClient.update(updateRequest);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteSingleDataFromEs(int houseId) {
        DeleteRequest deleteRequest = new DeleteRequest();
        deleteRequest.index("search_house").type("house").id(String.valueOf(houseId));

        try {
            restHighLevelClient.delete(deleteRequest);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
