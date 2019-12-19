package com.lch.lottery.search.msg;

import com.alibaba.fastjson.JSON;
import com.lch.lottery.search.service.PostService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ConsumerPostPage {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConsumerPostPage.class);
    @Autowired
    PostService postService;

    @RabbitListener(queues = {"${xuecheng.mq.queue}"})
    public void postPage(String msg) {
        //解析消息
        Map map = JSON.parseObject(msg, Map.class);
        //得到消息中的页面id
        String action = (String) map.get("action");
        //校验页面是否合法

        if (action.equals("write")) {
            postService.writePost((String) map.get("content"), (String) map.get("title"),
                    (String) map.get("userId"), (String) map.get("postId"));
        } else if (action.equals("update")) {
            postService.updatePost((String) map.get("content"), (String) map.get("title")
                    , (String) map.get("postId"));
        } else if (action.equals("delete")) {
            postService.deletePost((String) map.get("postId"));
        }

    }
}
