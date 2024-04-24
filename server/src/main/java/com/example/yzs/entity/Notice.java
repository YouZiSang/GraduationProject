package com.example.yzs.entity;

import lombok.Data;

import javax.persistence.Entity;

@Entity
@Data
public class Notice {
    private Integer id;
    private Integer aid;
    private String title;
    private String content;
    private String firstImage;
    private String secondImage;
    private String thirdImage;
    private String fourthImage;
    private Integer totalSettle;
    private String createdTime;
    private String updatedTime;
}
