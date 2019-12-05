package com.lch.lottery.post.dao;

import com.lch.lottery.post.entity.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by Administrator.
 */
public interface PostRepository extends JpaRepository<PostEntity,String> {

    List<PostEntity> findByUserId(String userId);
   PostEntity findByPostId(String postId);

}
