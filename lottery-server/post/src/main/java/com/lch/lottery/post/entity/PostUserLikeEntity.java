package com.lch.lottery.post.entity;

import javax.persistence.*;

@Entity
public class PostUserLikeEntity {//uid->role;role->permission
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    public Integer _id;

    public String postId;

    public String userId;


}
