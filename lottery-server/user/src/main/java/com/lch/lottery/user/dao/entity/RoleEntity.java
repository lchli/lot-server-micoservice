package com.lch.lottery.user.dao.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class RoleEntity {//uid->role;role->permission
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    public Integer _id;

    public String roleId;

    public String roleName;

}
