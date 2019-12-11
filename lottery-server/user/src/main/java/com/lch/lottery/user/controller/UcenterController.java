package com.lch.lottery.user.controller;

import com.lch.lottery.common.model.LotUser;
import com.lch.lottery.user.model.RegisterResponse;
import com.lch.lottery.user.dao.entity.UserEntity;
import com.lch.lottery.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * @author Administrator
 * @version 1.0
 **/
@RestController
@RequestMapping("/ucenter")
public class UcenterController {
    @Autowired
    UserService userService;

    @GetMapping("/getuserext")
    public LotUser getUserext(@RequestParam("username") String username) {
        return userService.getUserExt(username);
    }


    @GetMapping("/getUserById")
    public LotUser getUserById(@RequestParam("userId") String userId) {
        return userService.getUserById(userId);
    }



    @PostMapping(value = "/user",produces = {MediaType.APPLICATION_JSON_VALUE})
    public RegisterResponse register(@RequestParam("username") String username, @RequestParam("pwd") String pwd,
                            @RequestParam("headUrl") String headUrl) {
        UserEntity userEntity = new UserEntity();
        userEntity.headUrl = headUrl;
        userEntity.username = username;
        userEntity.pwd =new BCryptPasswordEncoder().encode(pwd); //pwd;
        userEntity.uid = UUID.randomUUID().toString();

        userEntity = userService.save(userEntity);

        RegisterResponse response = new RegisterResponse();
        if (userEntity == null) {
            response.code = "-1";
            response.errmsg = "reg fail.";
            return response;
        }
        response.code = "0";
        response.headUrl = userEntity.headUrl;
        response.name = userEntity.username;
        response.uid = userEntity.uid;

        return response;
    }
}
