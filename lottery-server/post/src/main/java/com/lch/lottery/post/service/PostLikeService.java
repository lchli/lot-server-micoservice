package com.lch.lottery.post.service;

import com.lch.lottery.post.dao.PostLikeRepository;
import com.lch.lottery.post.dao.PostRepository;
import com.lch.lottery.post.entity.PostEntity;
import com.lch.lottery.post.entity.PostUserLikeEntity;
import com.lch.lottery.post.mapper.Mappers;
import com.lch.lottery.post.model.PostLikeModel;
import com.lch.lottery.post.model.PostModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostLikeService {
    @Autowired
    private PostLikeRepository postRepository;
    @Autowired
    private Mappers mappers;


    public PostLikeModel save(PostLikeModel model) {
        PostUserLikeEntity entity = mappers.toPostLikeEntity(model);
        if (entity == null) {
            return null;
        }

        return mappers.toPostLikeModel(postRepository.save(entity));

    }


    public PostLikeModel findByUserIdAndPostId(String userId, String postId) {
        return mappers.toPostLikeModel(postRepository.findByUserIdAndPostId(userId, postId));
    }


    public  void deleteByUserIdAndPostId(String userId, String postId){
        postRepository.deleteByUserIdAndPostId(userId,postId);
    }


}
