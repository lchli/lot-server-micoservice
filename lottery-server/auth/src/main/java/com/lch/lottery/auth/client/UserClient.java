package com.lch.lottery.auth.client;

import com.lch.lottery.common.model.LotUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by Administrator.
 */
@FeignClient(value = "lot-service-ucenter")
public interface UserClient {
    //根据账号查询用户信息
    @GetMapping("/ucenter/getuserext")
    public LotUser getUserext(@RequestParam("username") String username);
}
