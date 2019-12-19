package com.lch.lottery.post.controller;

import com.alibaba.fastjson.JSON;
import com.lch.lottery.common.reponse.LotteryBaseResponse;
import com.lch.lottery.common.util.XcOauth2Util;
import com.lch.lottery.post.client.PostSearchClient;
import com.lch.lottery.post.config.RabbitmqConfig;
import com.lch.lottery.post.dao.PostLikeRepository;
import com.lch.lottery.post.dao.PostRepository;
import com.lch.lottery.post.entity.PostEntity;
import com.lch.lottery.post.entity.PostUserLikeEntity;
import com.lch.lottery.post.mapper.Mappers;
import com.lch.lottery.post.response.LikePostResponse;
import com.lch.lottery.post.response.PostsResponse;
import com.lch.lottery.post.response.SinglePostResponse;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author Administrator
 * @version 1.0
 **/
@RestController
@RequestMapping("/post")
public class PostController {

    @Autowired
    private PostLikeRepository postLikeRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private Mappers mappers;

    @Autowired
    RabbitTemplate rabbitTemplate;



    //当用户拥有course_teachplan_list权限时候方可访问此方法
    //@PreAuthorize("hasAuthority('hello2')")
    //@PreAuthorize("hasAuthority('hello')")//这个能访问。
    @PostMapping("/writePost")
    public SinglePostResponse writePost(@RequestParam("content") String content, @RequestParam("title") String title, HttpServletRequest request) {
        // String aAuthorization = request.getHeader("Authorization");
        SinglePostResponse response = new SinglePostResponse();

        XcOauth2Util xcOauth2Util = new XcOauth2Util();
        XcOauth2Util.UserJwt userJwt = xcOauth2Util.getUserJwtFromHeader(request);
        if (userJwt == null) {
            response.markErrorCode();
            response.errmsg = "token无效";
            return response;
        }

        PostEntity model = new PostEntity();
        model.content = content;
        model.title = title;
        model.userId = userJwt.getId();
        model.postId = UUID.randomUUID().toString();

        model = postRepository.save(model);
        if (model == null) {
            response.markErrorCode();
            response.errmsg = "发帖失败";
            return response;
        }

        response.post = mappers.toPostModel(model);

        //创建消息对象
        Map<String,String> msg = new HashMap<>();
        msg.put("action","write");
        msg.put("content",model.content);
        msg.put("title",model.title);
        msg.put("userId",model.userId);
        msg.put("postId",model.postId);
        //转成json串
        String jsonString = JSON.toJSONString(msg);
        //发送给mq
        //站点id
        //String siteId = cmsPage.getSiteId();
        rabbitTemplate.convertAndSend(RabbitmqConfig.EX_ROUTING_CMS_POSTPAGE,"5b30cba5f58b4411fc6cb1e5",jsonString);

        return response;
    }


    @PostMapping("/updatePost")
    public SinglePostResponse updatePost(@RequestParam("content") String content, @RequestParam("title") String title, @RequestParam(value = "postId") String postId, HttpServletRequest request) {
        // String aAuthorization = request.getHeader("Authorization");
        SinglePostResponse response = new SinglePostResponse();

        XcOauth2Util xcOauth2Util = new XcOauth2Util();
        XcOauth2Util.UserJwt userJwt = xcOauth2Util.getUserJwtFromHeader(request);
        if (userJwt == null) {
            response.markErrorCode();
            response.errmsg = "token无效";
            return response;
        }
        PostEntity old = postRepository.findByPostId(postId);
        if (old == null) {
            response.markErrorCode();
            response.errmsg = "帖子不存在";
            return response;
        }

        old.content = content;
        old.title = title;


        old = postRepository.save(old);
        if (old == null) {
            response.markErrorCode();
            response.errmsg = "发帖失败";
            return response;
        }

        response.post = mappers.toPostModel(old);

        //创建消息对象
        Map<String,String> msg = new HashMap<>();
        msg.put("action","update");
        msg.put("content",old.content);
        msg.put("title",old.title);
        msg.put("userId",old.userId);
        msg.put("postId",old.postId);
        //转成json串
        String jsonString = JSON.toJSONString(msg);
        //发送给mq
        //站点id
        //String siteId = cmsPage.getSiteId();
        rabbitTemplate.convertAndSend(RabbitmqConfig.EX_ROUTING_CMS_POSTPAGE,"5b30cba5f58b4411fc6cb1e5",jsonString);

        return response;
    }


    @PostMapping("/likePost")
    public LikePostResponse likePost(@RequestParam(value = "postId") String postId, HttpServletRequest request) {
        // String aAuthorization = request.getHeader("Authorization");
        LikePostResponse response = new LikePostResponse();

        XcOauth2Util xcOauth2Util = new XcOauth2Util();
        XcOauth2Util.UserJwt userJwt = xcOauth2Util.getUserJwtFromHeader(request);
        if (userJwt == null) {
            response.markErrorCode();
            response.errmsg = "token无效";
            return response;
        }

        PostUserLikeEntity entity = postLikeRepository.findByUserIdAndPostId(userJwt.getId(), postId);
        if (entity != null) {
            postLikeRepository.delete(entity);

            List<PostUserLikeEntity> likes = postLikeRepository.findByPostId(postId);
            response.postLikeCount = likes != null ? likes.size() : 0;

            return response;
        }

        entity = new PostUserLikeEntity();
        entity.userId = userJwt.getId();
        entity.postId = postId;

        entity = postLikeRepository.save(entity);
        if (entity == null) {
            response.markErrorCode();
            response.errmsg = "操作失败";
            return response;
        }

        List<PostUserLikeEntity> likes = postLikeRepository.findByPostId(postId);
        response.postLikeCount = likes != null ? likes.size() : 0;

        return response;
    }


    @GetMapping("/getPosts")
    public PostsResponse getPosts() {
        PostsResponse response = new PostsResponse();
        response.posts = mappers.toPostModels(postRepository.findAll());

        return response;
    }


    @GetMapping("/getPostById")
    public SinglePostResponse getPostById(@RequestParam("postId") String postId) {
        SinglePostResponse response = new SinglePostResponse();
        response.post = mappers.toPostModel(postRepository.findByPostId(postId));

        return response;

    }

    @PostMapping("/deletePostById")
    public LotteryBaseResponse deletePostById(@RequestParam("postId") String postId) {
        LotteryBaseResponse response = new LotteryBaseResponse();
        postRepository.deleteById(postId);

        //创建消息对象
        Map<String,String> msg = new HashMap<>();
        msg.put("action","delete");

        msg.put("postId",postId);
        //转成json串
        String jsonString = JSON.toJSONString(msg);
        //发送给mq
        //站点id
        //String siteId = cmsPage.getSiteId();
        rabbitTemplate.convertAndSend(RabbitmqConfig.EX_ROUTING_CMS_POSTPAGE,"5b30cba5f58b4411fc6cb1e5",jsonString);

        return response;

    }

}
