package com.ncut.backmanagement.service.impl;

import com.google.common.collect.Lists;
import com.ncut.backmanagement.common.HouseSuggest;
import com.ncut.backmanagement.common.ServiceMultiResult;
import com.ncut.backmanagement.common.ServiceResult;
import com.ncut.backmanagement.common.VO.HouseDTO;
import com.ncut.backmanagement.common.VO.HouseDetailDTO;
import com.ncut.backmanagement.dao.HouseDetailMapper;
import com.ncut.backmanagement.dao.HouseMapper;
import com.ncut.backmanagement.domain.House;
import com.ncut.backmanagement.domain.HouseDetail;
import com.ncut.backmanagement.domain.HouseIndexTemplate;
import com.ncut.backmanagement.domain.RentSearch;
import com.ncut.backmanagement.service.IHouseService;
import com.ncut.backmanagement.service.SearchDataService;
import org.elasticsearch.action.admin.indices.analyze.AnalyzeAction;
import org.elasticsearch.action.admin.indices.analyze.AnalyzeRequestBuilder;
import org.elasticsearch.action.admin.indices.analyze.AnalyzeResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.search.suggest.Suggest;
import org.elasticsearch.search.suggest.SuggestBuilder;
import org.elasticsearch.search.suggest.SuggestBuilders;
import org.elasticsearch.search.suggest.completion.CompletionSuggestion;
import org.elasticsearch.search.suggest.completion.CompletionSuggestionBuilder;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import java.util.*;

/**
 * <b>System：</b>ncc<br/>
 * <b>Title：</b>SearchDataServiceImpl<br/>
 * <b>Description：</b><br/>
 * <b>@author： </b>xiadong<br/>
 * <b>@date：</b>2019/8/22 23:00<br/>
 */
@Service
public class SearchDataServiceImpl implements SearchDataService {
    private static final Logger logger = LoggerFactory.getLogger(SearchDataServiceImpl.class);
    @Autowired
    private IHouseService iHouseService;
    @Autowired
    private HouseMapper houseMapper;
/*    @Autowired
    private HouseTagMapper houseTagMapper;*/
    @Autowired
    private HouseDetailMapper houseDetailMapper;

    @Autowired
    private RestHighLevelClient restHighLevelClient;
    @Autowired
    private TransportClient esClient;
    /**
     * 该方法实现需结合elasticsearch 和 mysql
     * @param houseSearch
     * @return
     */
    @Override
    public ServiceMultiResult<HouseDTO> searchData(RentSearch houseSearch) {
        if(StringUtils.isEmpty(houseSearch.getKeywords())){
            houseSearch.setKeywords("一期");
        }
        // 1、当用户没有输入和选择搜索相关的功能时，则不走搜索引擎，直接查询数据库，进行数据的返回
        if (StringUtils.isEmpty(houseSearch.getKeywords())) {

        }else{
        // 2、当选择了搜索的功能，则先走搜索引擎，在搜索引擎中查到id后，再根据id去数据库中查询数据返回
            ServiceMultiResult<Integer> serviceMultiResult = iHouseService.searchData(houseSearch);
            if (0 == serviceMultiResult.getTotal()){
                return new ServiceMultiResult<>(0, null);
            }
            // 查数据库
            return new ServiceMultiResult<>(serviceMultiResult.getTotal(), getHouseResult(serviceMultiResult.getResult()));
        }


        return null;
    }

    /**
     * 自动补全搜索
     * @param prefix
     * @return
     */
    @Override
    public ServiceResult<List<String>> suggest(String prefix) {
        CompletionSuggestionBuilder suggestion = SuggestBuilders.completionSuggestion("suggest").prefix(prefix).size(5);

        SuggestBuilder suggestBuilder = new SuggestBuilder();
        suggestBuilder.addSuggestion("autocomplete", suggestion);

        SearchRequestBuilder searchRequest = esClient.prepareSearch("search_house")
                .setTypes("house").suggest(suggestBuilder);

        SearchResponse response = searchRequest.get();
        Suggest suggest = response.getSuggest();
        if(suggest == null){
            return ServiceResult.of(new ArrayList<>());
        }
        Suggest.Suggestion result = suggest.getSuggestion("autocomplete");

        int maxSuggest = 0;
        Set<String> suggestSet = new HashSet<>();

        for (Object term : result.getEntries()) {
            if (term instanceof CompletionSuggestion.Entry) {
                CompletionSuggestion.Entry item = (CompletionSuggestion.Entry) term;

                if (item.getOptions().isEmpty()) {
                    continue;
                }

                for (CompletionSuggestion.Entry.Option option : item.getOptions()) {
                    String tip = option.getText().string();
                    if (suggestSet.contains(tip)) {
                        continue;
                    }
                    suggestSet.add(tip);
                    maxSuggest++;
                }
            }

            if (maxSuggest > 5) {
                break;
            }
        }
        List<String> suggests = Lists.newArrayList(suggestSet.toArray(new String[]{}));
        return ServiceResult.of(suggests);
    }


    /**
     * 根据房子id查询数据库中数据
     * @param houseIds
     * @return
     */
    private List<HouseDTO> getHouseResult(List<Integer> houseIds) {
        List<HouseDTO> result = new ArrayList<>();

        Map<Integer, HouseDTO> idToHouseMap = new HashMap<>();
        List<House> houses = houseMapper.selectBatchIds(houseIds);
        houses.forEach(house -> {
            HouseDTO houseDTO = new ModelMapper().map(house, HouseDTO.class);
            idToHouseMap.put(house.getId(), houseDTO);
        });

        getHouseList(houseIds, idToHouseMap);

        // 矫正顺序
        for (Integer houseId : houseIds) {
            result.add(idToHouseMap.get(houseId));
        }
        return result;
    }

    /**
     * 根据房子id查询房子详细信息 及 标签
     * @param houseIds
     * @param idToHouseMap
     */
    private void getHouseList(List<Integer> houseIds, Map<Integer,HouseDTO> idToHouseMap) {
        List<HouseDetail> houseDetails = new ArrayList<>();
        houseIds.forEach(houseId ->{
            HouseDetail houseDetail1 = new HouseDetail();
            houseDetail1.setHouseId(houseId);
            HouseDetail houseDetail  = houseDetailMapper.selectOne(houseDetail1);
            houseDetails.add(houseDetail);
        });

        // 将房屋详情赋值给房屋
        houseDetails.forEach(houseDetail -> {
            idToHouseMap.get(houseDetail.getHouseId())
                    .setHouseDetail(new ModelMapper().map(houseDetail, HouseDetailDTO.class));
        });


    }
}
