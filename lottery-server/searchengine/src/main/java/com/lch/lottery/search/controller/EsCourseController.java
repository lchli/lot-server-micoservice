package com.lch.lottery.search.controller;


import com.lch.lottery.common.model.LotUser;
import com.lch.lottery.common.reponse.LotteryBaseResponse;
import com.lch.lottery.search.client.UserClient;
import com.lch.lottery.search.model.SearchParam;
import com.lch.lottery.search.response.SearchPostResponse;
import com.lch.lottery.search.service.PostService;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 * @version 1.0
 **/
@RestController
@RequestMapping("/search/post")
public class EsCourseController {

    @Value("${xuecheng.course.index}")
    private String index;
    @Value("${xuecheng.media.index}")
    private String media_index;
    @Value("${xuecheng.course.type}")
    private String type;
    @Value("${xuecheng.media.type}")
    private String media_type;
    @Value("${xuecheng.course.source_field}")
    private String source_field;
    @Value("${xuecheng.media.source_field}")
    private String media_source_field;

    @Autowired
    RestHighLevelClient restHighLevelClient;
    @Autowired
    PostService postService;
    @Autowired
    UserClient userClient;


    @GetMapping(value = "/list")
    public SearchPostResponse searchPost(@RequestParam("page") int page, @RequestParam("size") int size,
                                         @RequestParam("keyword") String keyword, @RequestParam("sort") String sort) {
      System.err.println("keyword:"+keyword);
        //创建搜索请求对象
        SearchRequest searchRequest = new SearchRequest(index);
        //设置搜索类型
        searchRequest.types(type);

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //过虑源字段
        String[] source_field_array = source_field.split(",");
        searchSourceBuilder.fetchSource(source_field_array, new String[]{});
        //创建布尔查询对象
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        //搜索条件
        //根据关键字搜索
        if (StringUtils.isNotEmpty(keyword)) {
            MultiMatchQueryBuilder multiMatchQueryBuilder = QueryBuilders.multiMatchQuery(keyword, "title", "content")
                    .minimumShouldMatch("70%")
                    .field("title", 10);

            boolQueryBuilder.must(multiMatchQueryBuilder);
        }

        //设置boolQueryBuilder到searchSourceBuilder
        searchSourceBuilder.query(boolQueryBuilder);
        //设置分页参数
        if (page <= 0) {
            page = 1;
        }
        if (size <= 0) {
            size = 12;
        }
        //起始记录下标
        int from = (page - 1) * size;
        searchSourceBuilder.from(from);
        searchSourceBuilder.size(size);

        //设置高亮
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.preTags("<font class='eslight'>");
        highlightBuilder.postTags("</font>");
        //设置高亮字段
        highlightBuilder.fields().add(new HighlightBuilder.Field("title"));
        searchSourceBuilder.highlighter(highlightBuilder);

        searchRequest.source(searchSourceBuilder);

        SearchPostResponse queryResult = new SearchPostResponse();
        List<SearchPostResponse.Item> list = new ArrayList<>();
        try {
            //执行搜索
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest);
            //获取响应结果
            SearchHits hits = searchResponse.getHits();
            //匹配的总记录数
            long totalHits = hits.totalHits;
            // queryResult.setTotal(totalHits);
            SearchHit[] searchHits = hits.getHits();
            for (SearchHit hit : searchHits) {
                SearchPostResponse.Item coursePub = new SearchPostResponse.Item();
                //源文档
                Map<String, Object> sourceAsMap = hit.getSourceAsMap();
                //取出id
                String id = (String) sourceAsMap.get("postId");
                String userId = (String) sourceAsMap.get("userId");
                coursePub.postId = id;
                coursePub.userId = userId;

                LotUser user = userClient.getUserById(userId);
                if(user!=null){
                    coursePub.userName=user.getUsername();
                }
                //取出name
                String name = (String) sourceAsMap.get("title");
                //取出高亮字段name
                Map<String, HighlightField> highlightFields = hit.getHighlightFields();
                if (highlightFields != null) {
                    HighlightField highlightFieldName = highlightFields.get("title");
                    if (highlightFieldName != null) {
                        Text[] fragments = highlightFieldName.fragments();
                        StringBuffer stringBuffer = new StringBuffer();
                        for (Text text : fragments) {
                            stringBuffer.append(text);
                        }
                        name = stringBuffer.toString();
                    }

                }
                coursePub.title = name;

                //将coursePub对象放入list
                list.add(coursePub);
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

        queryResult.items = list;

        return queryResult;
    }


    @PostMapping("/writePost")
    public LotteryBaseResponse writePost(@RequestParam("content") String content, @RequestParam("title") String title,
                                         @RequestParam("userId") String userId, @RequestParam("postId") String postId) {

        return postService.writePost(content, title, userId, postId);

    }


    //todo use msg queue to sync...
    @PostMapping("/updatePost")
    public LotteryBaseResponse updatePost(@RequestParam("content") String content, @RequestParam("title") String title,
                                          @RequestParam("postId") String postId) {

        return postService.updatePost(content, title, postId);

    }

    @PostMapping("/deletePost")
    public LotteryBaseResponse deletePost(@RequestParam("postId") String postId) {

        return postService.deletePost(postId);

    }

}
