package com.lch.lottery.post.entity;

import javax.persistence.*;

@Entity
public class PostEntity {//uid->role;role->permission
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    public Integer _id;

    public String postId;

    public String userId;

    @Column(columnDefinition = "MEDIUMTEXT")
    public String content;

    public String title;

}
