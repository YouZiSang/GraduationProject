package com.example.yzs.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Transient;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Account {
    private Integer id;
    private Integer uid;
    private String password;
    private String role;
    private Integer deleted;
    private java.sql.Timestamp createTime;
    private LocalDateTime updateTime;
    @Transient
    private String verCode;
}
