package com.lch.lottery.post.mapper;

import com.lch.lottery.common.model.LotUser;
import com.lch.lottery.post.client.UserClient;
import com.lch.lottery.post.dao.PostLikeRepository;
import com.lch.lottery.post.dao.PostRepository;
import com.lch.lottery.post.entity.PostEntity;
import com.lch.lottery.post.entity.PostUserLikeEntity;
import com.lch.lottery.post.model.PostLikeModel;
import com.lch.lottery.post.model.PostModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
@Component
public class Mappers {
    @Autowired
    UserClient userClient;
    @Autowired
    private PostLikeRepository postLikeRepository;

    @Autowired
    private PostRepository postRepository;


    public  PostModel toPostModel(PostEntity entity) {
        if (entity == null) {
            return null;
        }
        PostModel model = new PostModel();
        model.content = entity.content;
        model.postId = entity.postId;
        model.title = entity.title;
        model.userId = entity.userId;

        LotUser user = userClient.getUserById(entity.userId);
        if (user != null) {
            model.userName = user.getUsername();
        }

        List<PostUserLikeEntity> likes = postLikeRepository.findByPostId(entity.postId);
        model.postLikeCount = likes != null ? likes.size() : 0;

        return model;
    }


    public  PostEntity toPostEntity(PostModel model) {
        if (model == null) {
            return null;
        }
        PostEntity entity = new PostEntity();
        entity.content = model.content;
        entity.postId = model.postId;
        entity.title = model.title;
        entity.userId = model.userId;

        return entity;
    }


    public  List<PostModel> toPostModels(List<PostEntity> entities) {
        if (entities == null || entities.isEmpty()) {
            return null;
        }
        List<PostModel> models = new ArrayList<>();
        for (PostEntity entity : entities) {
            models.add(toPostModel(entity));
        }

        return models;
    }


    public  PostLikeModel toPostLikeModel(PostUserLikeEntity entity) {
        if (entity == null) {
            return null;
        }
        PostLikeModel model = new PostLikeModel();
        model.postId = entity.postId;
        model.userId = entity.userId;

        return model;
    }

    public  PostUserLikeEntity toPostLikeEntity(PostLikeModel model) {
        if (model == null) {
            return null;
        }
        PostUserLikeEntity entity = new PostUserLikeEntity();
        entity.postId = model.postId;
        entity.userId = model.userId;

        return entity;
    }


}
