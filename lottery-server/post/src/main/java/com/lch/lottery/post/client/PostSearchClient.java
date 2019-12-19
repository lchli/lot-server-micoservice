package com.lch.lottery.post.client;

import com.lch.lottery.common.model.LotUser;
import com.lch.lottery.common.reponse.LotteryBaseResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by Administrator.
 */
@FeignClient(value = "lot-service-search")
public interface PostSearchClient {

    @PostMapping("/search/post/writePost")
    public LotteryBaseResponse writePost(@RequestParam("content") String content, @RequestParam("title") String title,
                                         @RequestParam("userId") String userId, @RequestParam("postId") String postId);


    @PostMapping("/search/post/updatePost")
    public LotteryBaseResponse updatePost(@RequestParam("content") String content, @RequestParam("title") String title,
                                          @RequestParam("postId") String postId);


    @PostMapping("/search/post/deletePost")
    public LotteryBaseResponse deletePost(@RequestParam("postId") String postId);


}
