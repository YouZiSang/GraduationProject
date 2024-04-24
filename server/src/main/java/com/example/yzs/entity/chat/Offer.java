package com.example.yzs.entity.chat;

import lombok.Data;

import javax.persistence.Entity;

@Entity
@Data
public class Offer {
    private String type;
    private String sdp;
}
