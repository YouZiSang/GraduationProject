package com.example.yzs.entity;

import lombok.Data;

import javax.persistence.Transient;

@Data
public class User {
    private Integer id;
    private Integer userId;    //账号
    private String userPassword;   //密码
    @Transient
    private String verCode;

    public User() {
    }

    public User(Integer id,Integer userId, String userPassword) {
        this.id = id;
        this.userId = userId;
        this.userPassword = userPassword;
    }
}
