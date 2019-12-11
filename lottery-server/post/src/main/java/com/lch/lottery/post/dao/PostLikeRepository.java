package com.lch.lottery.post.dao;

import com.lch.lottery.post.entity.PostUserLikeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by Administrator.
 */
public interface PostLikeRepository extends JpaRepository<PostUserLikeEntity, String> {

    PostUserLikeEntity findByUserIdAndPostId(String userId, String postId);
    List<PostUserLikeEntity> findByPostId(String postId);

    void deleteByUserIdAndPostId(String userId, String postId);
}
