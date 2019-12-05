package com.lch.lottery.common.model;

import java.util.List;

public class LotUser {
    private String userId;
    private String username;
    private String password;
    private List<String> permissionIds;


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<String> getPermissions() {
        return permissionIds;
    }

    public void setPermissions(List<String> permissions) {
        this.permissionIds = permissions;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
