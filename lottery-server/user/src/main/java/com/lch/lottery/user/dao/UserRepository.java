package com.lch.lottery.user.dao;

import com.lch.lottery.user.dao.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Administrator.
 */
public interface UserRepository extends JpaRepository<UserEntity,String> {
    //根据账号查询用户信息
    UserEntity findByUsername(String username);

}
