package com.example.yzs.entity;

import lombok.Data;

import javax.persistence.Entity;

@Entity
@Data
public class UserInfo {

    private Integer id;
    private Integer uid;    //账号
    private String name;    //姓名
    private String avatarUrl;  //头像
    private String nickname;  //昵称
    private String sex;     //性别
    private Integer age;        //年龄
    private String email;   //邮箱
    private Long phone;      //电话
    private String deliveryAdd;    //收货地址
    private Integer deleted;
}
