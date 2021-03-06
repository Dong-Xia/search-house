package com.ncut.backmanagement.config;

import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.get.*;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.*;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.Strings;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * <b>System：</b>ncc<br/>
 * <b>Title：</b>ElasticSearchConfigTest<br/>
 * <b>Description：</b><br/>
 * <b>@author： </b>xiadong<br/>
 * <b>@date：</b>2019/8/10 14:26<br/>
 */
public class ElasticSearchConfigTest {


    /**
     * 获取单条数据
     */
    @Test
    public void getData() {
        AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext(ElasticSearchConfig.class);
        RestClientBuilder bean = annotationConfigApplicationContext.getBean(RestClientBuilder.class);
        System.out.println(bean);

        RestHighLevelClient bean1 = annotationConfigApplicationContext.getBean(RestHighLevelClient.class);
        System.out.println(bean1);

        // 1、创建获取文档请求
        GetRequest request = new GetRequest(
                "book",   //索引
                "novel",     // mapping type
                "1");     //文档id

        //选择返回的字段
        String[] includes = new String[]{"author", "name"};
        String[] excludes = Strings.EMPTY_ARRAY;
        FetchSourceContext fetchSourceContext = new FetchSourceContext(true, includes, excludes);
        request.fetchSourceContext(fetchSourceContext);

        //3、发送请求
        GetResponse getResponse = null;
        try {
            // 同步请求
            getResponse = bean1.get(request);
        } catch (ElasticsearchException e) {
            if (e.status() == RestStatus.NOT_FOUND) {
                System.out.println("没有找到该id的文档" );
            }
            if (e.status() == RestStatus.CONFLICT) {
                System.out.println("获取时版本冲突了，请在此写冲突处理逻辑！" );
            }
            System.out.println("获取文档异常");
        } catch (IOException e) {
            e.printStackTrace();
        }

        //4、处理响应
        if(getResponse != null) {
            String index = getResponse.getIndex();
            String type = getResponse.getType();
            String id = getResponse.getId();
            if (getResponse.isExists()) { // 文档存在
                long version = getResponse.getVersion();
                String sourceAsString = getResponse.getSourceAsString(); //结果取成 String
                Map<String, Object> sourceAsMap = getResponse.getSourceAsMap();  // 结果取成Map
                byte[] sourceAsBytes = getResponse.getSourceAsBytes();    //结果取成字节数组

                System.out.println("index:" + index + "  type:" + type + "  id:" + id);
                System.out.println(sourceAsString);

            } else {
                System.out.println("没有找到该id的文档" );
            }
        }
    }

    /**
     * 批量获取数据（只返回数据部分需要的字段）
     */
    @Test
    public void getMultiData() {
        AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext(ElasticSearchConfig.class);

        RestHighLevelClient restHighLevelClient = annotationConfigApplicationContext.getBean(RestHighLevelClient.class);
        System.out.println(restHighLevelClient);

        //只返回数据部分需要的字段
        String[] includes = new String[] {"name", "author"};
        String[] excludes = Strings.EMPTY_ARRAY;
        FetchSourceContext fetchSourceContext = new FetchSourceContext(true, includes, excludes);

        MultiGetRequest multiGetRequest = new MultiGetRequest();
        multiGetRequest.add(new MultiGetRequest.Item("book", "novel", "1").fetchSourceContext(fetchSourceContext));
        multiGetRequest.add(new MultiGetRequest.Item("book", "novel", "2").fetchSourceContext(fetchSourceContext));
        multiGetRequest.add(new MultiGetRequest.Item("book", "novel", "4").fetchSourceContext(fetchSourceContext));

        MultiGetResponse result = null;

        try {
            result = restHighLevelClient.multiGet(multiGetRequest);
            System.out.println(result);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //处理响应
        if (result != null) {
            MultiGetItemResponse[] multiGetItemResponses = result.getResponses();
            for (MultiGetItemResponse multiGetItemResponse : multiGetItemResponses) {
                String index = multiGetItemResponse.getIndex();
                String id = multiGetItemResponse.getId();
                String type = multiGetItemResponse.getType();
                System.out.println("index:" + index + "  type:" + type + "  id:" + id);
                System.out.println(multiGetItemResponse.getResponse().getSourceAsString());
            }
        }
    }


    /**
     * 添加数据
     */
    @Test
    public void addNewData() {
        AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext(ElasticSearchConfig.class);

        RestHighLevelClient restHighLevelClient = annotationConfigApplicationContext.getBean(RestHighLevelClient.class);

        IndexRequest indexRequest = new IndexRequest("book");
        Map<String, Object> map = new HashMap<>();
        map.put("score", 1);
        map.put("author", "令狐冲");
        map.put("price", 1909);
        map.put("name", "独孤九剑传承者");
        map.put("date", "2019-8-15");

        indexRequest.type("novel").id("9").source(map);

        try {
            restHighLevelClient.index(indexRequest);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除数据
     */
    @Test
    public void deleteData() {
        AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext(ElasticSearchConfig.class);

        RestHighLevelClient restHighLevelClient = annotationConfigApplicationContext.getBean(RestHighLevelClient.class);

        DeleteRequest deleteRequest = new DeleteRequest();
        deleteRequest.index("book"); // 指定es索引（类似数据库名）
        deleteRequest.type("novel");  // 指定es类型（类似表名）
        deleteRequest.id("11");  // 指定数据id

        try {
            restHighLevelClient.delete(deleteRequest);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 批量添加数据
     */
    @Test
    public void addBulkNewData() {
        AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext(ElasticSearchConfig.class);

        RestHighLevelClient restHighLevelClient = annotationConfigApplicationContext.getBean(RestHighLevelClient.class);

        // 添加数据操作类：IndexRequest
        IndexRequest indexRequest = new IndexRequest("book");
        Map<String, Object> map = new HashMap<>();
        map.put("score", 1);
        map.put("author", "令狐冲");
        map.put("price", 1909);
        map.put("name", "独孤九剑传承者");
        map.put("date", "2019-8-15");
        indexRequest.type("novel").id("9").source(map);

        IndexRequest indexRequest1 = new IndexRequest("book");
        Map<String, Object> map1 = new HashMap<>();
        map1.put("score", 1);
        map1.put("author", "小龙女");
        map1.put("price", 3000);
        map1.put("name", "古墓派玉女心经修炼者");
        map1.put("date", "2019-08-15");
        indexRequest1.type("novel").id("10").source(map1);

        IndexRequest indexRequest2 = new IndexRequest("book");
        Map<String, Object> map2 = new HashMap<>();
        map2.put("score", 1);
        map2.put("author", "林平之");
        map2.put("price", 800);
        map2.put("name", "辟邪剑谱修炼者");
        map2.put("date", "2019-08-14");
        indexRequest2.type("novel").id("11").source(map2);

        BulkRequest bulkRequest = new BulkRequest();
        bulkRequest.add(indexRequest);
        bulkRequest.add(indexRequest1);
        bulkRequest.add(indexRequest2);

        try {
            restHighLevelClient.bulk(bulkRequest);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 批量更新数据
     */
    @Test
    public void updateBulkData() {
        AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext(ElasticSearchConfig.class);

        RestHighLevelClient restHighLevelClient = annotationConfigApplicationContext.getBean(RestHighLevelClient.class);

        // UpdateRequest更新数据类
        UpdateRequest updateRequest = new UpdateRequest("search_house","house","25");
        Map<String, Object> map = new HashMap<>();
        map.put("layoutDesc", "景色美丽");
        updateRequest.doc(map);

        UpdateRequest updateRequest1 = new UpdateRequest("search_house","house","15");
        Map<String, Object> map1 = new HashMap<>();
        map1.put("title", "古墓派万年老宅");
        updateRequest1.doc(map1);

        BulkRequest bulkRequest = new BulkRequest();
        bulkRequest.add(updateRequest);
        bulkRequest.add(updateRequest1);

        try {
            restHighLevelClient.bulk(bulkRequest);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 批量删除数据
     */
    @Test
    public void deleteBulkData() {
        AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext(ElasticSearchConfig.class);

        RestHighLevelClient restHighLevelClient = annotationConfigApplicationContext.getBean(RestHighLevelClient.class);

        // DeleteRequest删除数据类
        DeleteRequest deleteRequest = new DeleteRequest("book","novel","9");
        DeleteRequest deleteRequest1 = new DeleteRequest("book","novel","10");

        BulkRequest bulkRequest = new BulkRequest();
        bulkRequest.add(deleteRequest);
        bulkRequest.add(deleteRequest1);

        try {
            restHighLevelClient.bulk(bulkRequest);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 批量搜索数据(多条件查询)
     */
    @Test
    public void searchMutiData() {
        AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext(ElasticSearchConfig.class);
        RestHighLevelClient restHighLevelClient = annotationConfigApplicationContext.getBean(RestHighLevelClient.class);

        // 创建一个容纳多查询条件的空MultiSearchRequest
        MultiSearchRequest multiSearchRequest = new MultiSearchRequest();
        SearchRequest searchRequest = new SearchRequest("search_house");
        searchRequest.types("house");
        //查询条件设置
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchQuery("title","一期"));
        searchRequest.source(searchSourceBuilder);
        multiSearchRequest.add(searchRequest);

        // 创建第二个查询条件(和第一个查询条件是或的关系，即把满足第一个查询和第二个查询条件的都搜索出来)
        SearchRequest searchRequest1 = new SearchRequest("search_house");
        searchRequest1.types("house");
        SearchSourceBuilder searchSourceBuilder1 = new SearchSourceBuilder();
        searchSourceBuilder1.query(QueryBuilders.matchQuery("subwayLineName","10号线"));
        searchRequest1.source(searchSourceBuilder1);
        multiSearchRequest.add(searchRequest1);

        try {
            MultiSearchResponse items = restHighLevelClient.multiSearch(multiSearchRequest);
            MultiSearchResponse.Item[] responses = items.getResponses();
            for(MultiSearchResponse.Item item : responses){
                System.out.println(item.getResponse().toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 按搜索条件搜索数据
     */
    @Test
    public void searchDataForTerm() {
        AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext(ElasticSearchConfig.class);
        RestHighLevelClient restHighLevelClient = annotationConfigApplicationContext.getBean(RestHighLevelClient.class);

        //只返回数据部分需要的字段
        String[] includes = new String[] {"id","title", "layoutDesc","subwayLineName","createTime"};
        String[] excludes = Strings.EMPTY_ARRAY;
        FetchSourceContext fetchSourceContext = new FetchSourceContext(true, includes, excludes);

        SearchRequest searchRequest = new SearchRequest("search_house");
        searchRequest.types("house");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        // 设置过滤条件
        boolQueryBuilder.filter(QueryBuilders.termQuery("subwayLineName","13号线"));
        boolQueryBuilder.filter(QueryBuilders.matchQuery("title","一期"));
        //boolQueryBuilder.filter(QueryBuilders.matchQuery("layoutDesc","景色宜人"));

        // 关键：设置搜索用的关键词在哪些字段中去搜，因为用户搜索的时候是随便输入的，这样在实现的时候需要指定覆盖哪些字段
        // 参数：text为前台页面用户输入的关键词
        boolQueryBuilder.must(QueryBuilders.multiMatchQuery("13号线","title","subwayLineName"));
        searchSourceBuilder.query(boolQueryBuilder).fetchSource(fetchSourceContext).sort("createTime");
        searchRequest.source(searchSourceBuilder);

        try {
            SearchResponse search = restHighLevelClient.search(searchRequest);
            if (search.status() != RestStatus.OK){
                System.out.println("查询失败！");
            }
                System.out.println(search.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}