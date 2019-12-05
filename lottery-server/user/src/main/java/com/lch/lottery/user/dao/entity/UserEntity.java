package com.lch.lottery.user.dao.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class UserEntity {//uid->role;role->permission
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    public Integer _id;

    public String uid;

    public String username;
    public String pwd;
    public String headUrl;
}
