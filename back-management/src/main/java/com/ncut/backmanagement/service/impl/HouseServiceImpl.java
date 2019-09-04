package com.ncut.backmanagement.service.impl;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import com.ncut.backmanagement.common.HouseIndexKey;
import com.ncut.backmanagement.common.HouseSuggest;
import com.ncut.backmanagement.common.RentValueBlock;
import com.ncut.backmanagement.common.ServiceMultiResult;
import com.ncut.backmanagement.dao.HouseMapper;
import com.ncut.backmanagement.domain.House;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ncut.backmanagement.domain.HouseDetail;
import com.ncut.backmanagement.domain.HouseIndexTemplate;
import com.ncut.backmanagement.domain.RentSearch;
import com.ncut.backmanagement.service.IHouseService;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.admin.indices.analyze.AnalyzeAction;
import org.elasticsearch.action.admin.indices.analyze.AnalyzeRequestBuilder;
import org.elasticsearch.action.admin.indices.analyze.AnalyzeResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.transport.TransportClient;
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
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
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

    @Autowired
    private TransportClient esClient;

    /**
     * 索引名称
     */
    private static final String INDEX_NAME = "search_house";

    @Override
    public void addBulkDataToEs(List<House> houses,Map<Integer, HouseDetail> houseDetailMap) {
        // es批量操作类
        BulkRequest bulkRequest = new BulkRequest();
        // 只对常用于搜索的数据进行同步到es中，因为es进行搜索的时候会将数据从磁盘加载到内存中，内存空间有限
        houses.forEach(house -> {
            HouseDetail houseDetail = houseDetailMap.get(house.getId());
            HouseIndexTemplate houseIndexTemplate = new HouseIndexTemplate();
            ModelMapper modelMapper = new ModelMapper();
            modelMapper.map(house,houseIndexTemplate);
            modelMapper.map(houseDetail,houseIndexTemplate);
            updateSuggest(houseIndexTemplate);
            JSON parse = JSONUtil.parse(houseIndexTemplate);
            IndexRequest indexRequest = new IndexRequest();
            // 指定es索引、类型
            indexRequest.index("search_house").type("house").id(house.getId().toString());
            // 构建存入es的json文档
/*            Map<String, Object> map = new HashMap<>();
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
            indexRequest.source(map);*/
            indexRequest.source(parse.toString(), XContentType.JSON);
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
    public void updateSingleDataToEs(HouseIndexTemplate data, int houseId) {
        updateSuggest(data);
        UpdateRequest updateRequest = new UpdateRequest();
        updateRequest.index("search_house").type("house").id(String.valueOf(houseId));
        JSON parse = JSONUtil.parse(data);
        updateRequest.doc(parse.toString(), XContentType.JSON); //以JSON格式的字符串形式提供的部分文档源

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

    /**
     * 更新和生成每条数据自动补全的相关词语
     * @param indexTemplate
     * @return
     */
    private boolean updateSuggest(HouseIndexTemplate indexTemplate){
        // 分析需要用于自动补全的字段(这些字段一般为热数据，经常会被搜索的字段数据)
        AnalyzeRequestBuilder analyzeRequestBuilder = new AnalyzeRequestBuilder(esClient
                ,AnalyzeAction.INSTANCE,INDEX_NAME,indexTemplate.getTitle(),indexTemplate.getLayoutDesc()
                ,indexTemplate.getRoundService(),indexTemplate.getDescription()
                ,indexTemplate.getSubwayLineName(),indexTemplate.getSubwayStationName());
        // 设置分词器的分词粗细粒度
        //           a)ik_max_word: 会将文本做最细粒度的拆分，比如会将“中华人民共和国国歌”拆分为“中华人民共和国,中华人民,中华,华人,人民共和国,人民,人,民,共和国,共和,和,国国,国歌”，会穷尽各种可能的组合，适合 Term Query；
        //           b)ik_smart: 会做最粗粒度的拆分，比如会将“中华人民共和国国歌”拆分为“中华人民共和国,国歌”，适合 Phrase 查询。
        analyzeRequestBuilder.setAnalyzer("ik_smart");

        // 执行获取分词结果
        AnalyzeResponse response = analyzeRequestBuilder.get();
        List<AnalyzeResponse.AnalyzeToken> tokens = response.getTokens();
        if (tokens == null) {
            logger.warn("不能使用分词器得到该数据的分词结果： " + indexTemplate.getHouseId());
            return false;
        }

        // 保存分词结果
        List<HouseSuggest> suggests = new ArrayList<>();
        for (AnalyzeResponse.AnalyzeToken token: tokens) {
            // 排除数字类型(格式为<NUM>) 和 长度小于2的字符，这些自动补全没有意义
            if ("<NUM>".equals(token.getType()) || token.getTerm().length()<2) {
                continue;
            }
            HouseSuggest houseSuggest = new HouseSuggest();
            houseSuggest.setInput(token.getTerm());
            suggests.add(houseSuggest);
        }

        //对于没有进行分词的字段数据，可以按照自己的爱好设置进行 专项定制
        // 如：对房子的地区做自动化补全
        HouseSuggest districtHouseSuggest = new HouseSuggest();
        districtHouseSuggest.setInput(indexTemplate.getDistrict());
        suggests.add(districtHouseSuggest);

        // 将处理后保存好的分词结果放入HouseIndexTemplate类的对象中，用于同步至es中
        indexTemplate.setSuggest(suggests);
        return true;
    }
}
