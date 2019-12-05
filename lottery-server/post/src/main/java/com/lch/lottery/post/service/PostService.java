package com.lch.lottery.post.service;

import com.lch.lottery.post.dao.PostRepository;
import com.lch.lottery.post.entity.PostEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class PostService {
    @Autowired
    private PostRepository postRepository;


    public PostEntity save(PostEntity entity){
       return postRepository.save(entity);

    }


    public List<PostEntity> findByUserId(String userId){
        return postRepository.findByUserId(userId);
    }


    public List<PostEntity> findAll(){
        return postRepository.findAll();
    }

    public PostEntity findByPostId(String postId){
        return postRepository.findByPostId(postId);
    }
}
