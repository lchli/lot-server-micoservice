package com.lch.lottery.post.controller;

import com.lch.lottery.common.util.XcOauth2Util;
import com.lch.lottery.post.entity.PostEntity;
import com.lch.lottery.post.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.UUID;

/**
 * @author Administrator
 * @version 1.0
 **/
@RestController
@RequestMapping("/post")
public class PostController {
    @Autowired
    private PostService postService;

    //当用户拥有course_teachplan_list权限时候方可访问此方法
    //@PreAuthorize("hasAuthority('hello2')")
    //@PreAuthorize("hasAuthority('hello')")//这个能访问。
    @PostMapping("/writePost")
    public String writePost(@RequestParam("content") String content, @RequestParam("title") String title,HttpServletRequest request) {
        // String aAuthorization = request.getHeader("Authorization");
        XcOauth2Util xcOauth2Util = new XcOauth2Util();
        XcOauth2Util.UserJwt userJwt = xcOauth2Util.getUserJwtFromHeader(request);

        PostEntity entity=new PostEntity();
        entity.content=content;
        entity.title=title;
        entity.userId=userJwt.getId();
        entity.postId= UUID.randomUUID().toString();

        entity=postService.save(entity);

        return entity._id+"";
    }


    @GetMapping("/getPost")
    public List<PostEntity> getPost() {

        return postService.findAll();
    }


    @GetMapping("/getPostById")
    public PostEntity getPostById(@RequestParam("postId") String postId) {

        return postService.findByPostId(postId);
    }

}
