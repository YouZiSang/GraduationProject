package com.example.yzs.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class ConsultantsInfo {
    private Integer id;
    private Integer uid;    //账号
    private Integer cid;    //账号
    private String name;    //姓名
    private String avatarUrl;  //头像
    private String sex;     //性别
    private Integer age;        //年龄
    private String certificate;//证书
    private String biography;//个人简介
    private String email;   //邮箱
    private Long phone;      //电话
    private String deliveryAdd; //地址status
    private String status;

}
