package com.example.yzs.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "admin")
@Data
public class Admin {

    @Column(name = "admin_id")
    private Integer admin_id;
    @Column(name = "admin_password")
    private String admin_password;
    @Transient
    private String verCode;

}
