package com.ncut.backmanagement.service.impl;

import com.ncut.backmanagement.common.HouseIndexKey;
import com.ncut.backmanagement.common.RentValueBlock;
import com.ncut.backmanagement.common.ServiceMultiResult;
import com.ncut.backmanagement.dao.HouseMapper;
import com.ncut.backmanagement.domain.House;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ncut.backmanagement.domain.HouseDetail;
import com.ncut.backmanagement.domain.RentSearch;
import com.ncut.backmanagement.service.IHouseService;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.Strings;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
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
    private static final Logger logger = LoggerFactory.getLogger(HouseServiceImpl.class);

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

    @Override
    public ServiceMultiResult<Integer> searchData(RentSearch houseSearch) {
        //只返回指定需要的字段
        String[] includes = new String[] {"id"};
        String[] excludes = Strings.EMPTY_ARRAY;
        FetchSourceContext fetchSourceContext = new FetchSourceContext(true, includes, excludes);

        SearchRequest searchRequest = new SearchRequest("search_house");
        searchRequest.types("house");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        // 关键：设置搜索用的关键词在哪些字段中去搜，因为用户搜索的时候是随便输入的，这样在实现的时候需要指定覆盖哪些字段
        // 参数：text为前台页面用户输入的关键词
        boolQueryBuilder.must(QueryBuilders.multiMatchQuery(houseSearch.getKeywords(),
                HouseIndexKey.TITLE,
                HouseIndexKey.TRAFFIC,
                HouseIndexKey.DISTRICT,
                HouseIndexKey.ROUND_SERVICE,
                HouseIndexKey.SUBWAY_LINE_NAME,
                HouseIndexKey.SUBWAY_STATION_NAME
        ));

        // 设置过滤条件
        // 房间大小区间过滤
        RentValueBlock rentValueBlock = RentValueBlock.matchArea(houseSearch.getAreaBlock());
        if (!RentValueBlock.ALL.equals(rentValueBlock)) {
            RangeQueryBuilder rangeQueryBuilder = new RangeQueryBuilder(HouseIndexKey.AREA);
            if (rentValueBlock.getMin() > 0) {
                rangeQueryBuilder.gte(rentValueBlock.getMin());
            }
            if (rentValueBlock.getMax() > 0) {
                rangeQueryBuilder.lte(rentValueBlock.getMax());
            }
            boolQueryBuilder.filter(rangeQueryBuilder);
        }

        // 价格区间过滤
        RentValueBlock rentPrice = RentValueBlock.matchArea(houseSearch.getPriceBlock());
        if (!RentValueBlock.ALL.equals(rentPrice)) {
            RangeQueryBuilder rangeQueryBuilderForPrice = new RangeQueryBuilder(HouseIndexKey.PRICE);
            if (rentPrice.getMin() > 0) {
                rangeQueryBuilderForPrice.gte(rentPrice.getMin());
            }
            if (rentPrice.getMax() > 0) {
                rangeQueryBuilderForPrice.lte(rentPrice.getMax());
            }
            boolQueryBuilder.filter(rangeQueryBuilderForPrice);
        }


        if (houseSearch.getDirection() > 0) {
            boolQueryBuilder.filter(
                    QueryBuilders.termQuery(HouseIndexKey.DIRECTION, houseSearch.getDirection())
            );
        }

        if (houseSearch.getRentWay() > -1) {
            boolQueryBuilder.filter(
                    QueryBuilders.termQuery(HouseIndexKey.RENT_WAY, houseSearch.getRentWay())
            );
        }

        if (StringUtils.isNotEmpty(houseSearch.getCityEnName())) {
            boolQueryBuilder.filter(
                    QueryBuilders.termQuery(HouseIndexKey.CITY_EN_NAME, houseSearch.getCityEnName())
            );
        }

        if (houseSearch.getRegionEnName() != null && !"*".equals(houseSearch.getRegionEnName())) {
            boolQueryBuilder.filter(
                    QueryBuilders.termQuery(HouseIndexKey.REGION_EN_NAME, houseSearch.getRegionEnName())
            );
        }

        searchSourceBuilder.query(boolQueryBuilder).fetchSource(fetchSourceContext).sort("createTime");
        searchRequest.source(searchSourceBuilder);

        try {
            SearchResponse search = restHighLevelClient.search(searchRequest);
            if (search.status() != RestStatus.OK){
                logger.info("查询失败！");
                return new ServiceMultiResult<>(0,null);
            }
            List<Integer> houseIds = new ArrayList<>();
            SearchHits hits = search.getHits();
            for (SearchHit hit : hits) {
                houseIds.add(Integer.parseInt(hit.getId()));
            }
            return new ServiceMultiResult<>(Integer.parseInt(String.valueOf(hits.totalHits)), houseIds);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
