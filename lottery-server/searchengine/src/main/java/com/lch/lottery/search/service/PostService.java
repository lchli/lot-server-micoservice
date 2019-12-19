package com.lch.lottery.search.service;

import com.lch.lottery.common.reponse.LotteryBaseResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.rest.RestStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;
@Service
public class PostService {

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

    public LotteryBaseResponse writePost( String content, String title,
                                          String userId,  String postId) {

        LotteryBaseResponse response = new LotteryBaseResponse();

        try {

            Map<String, String> map = new HashMap<>();
            map.put("content", content);
            map.put("title", title);
            map.put("userId", userId);
            map.put("postId", postId);

            IndexRequest indexRequest = new IndexRequest(index, type).source(map);

            IndexResponse indexResp = restHighLevelClient.index(indexRequest);
            if (indexResp.status() == RestStatus.OK) {
                return response;
            }
            response.markErrorCode();
            response.errmsg = "code=" + indexResp.status().getStatus();

        } catch (Throwable e) {
            e.printStackTrace();
            response.markErrorCode();
            response.errmsg = e.getMessage();
        }

        return response;

    }

    public LotteryBaseResponse updatePost( String content,  String title,
                                          String postId) {

        LotteryBaseResponse response = new LotteryBaseResponse();

        try {

            Map<String, String> map = new HashMap<>();
            map.put("content", content);
            map.put("title", title);
            map.put("postId", postId);

            UpdateRequest indexRequest = new UpdateRequest(index, type, postId).doc(map);

            UpdateResponse indexResp = restHighLevelClient.update(indexRequest);
            if (indexResp.status() == RestStatus.OK) {
                return response;
            }
            response.markErrorCode();
            response.errmsg = "code=" + indexResp.status().getStatus();

        } catch (Throwable e) {
            e.printStackTrace();
            response.markErrorCode();
            response.errmsg = e.getMessage();
        }

        return response;

    }


    public LotteryBaseResponse deletePost( String postId) {

        LotteryBaseResponse response = new LotteryBaseResponse();

        try {

            DeleteRequest indexRequest = new DeleteRequest(index, type, postId);

            DeleteResponse indexResp = restHighLevelClient.delete(indexRequest);
            if (indexResp.status() == RestStatus.OK) {
                return response;
            }
            response.markErrorCode();
            response.errmsg = "code=" + indexResp.status().getStatus();

        } catch (Throwable e) {
            e.printStackTrace();
            response.markErrorCode();
            response.errmsg = e.getMessage();
        }

        return response;

    }
}
