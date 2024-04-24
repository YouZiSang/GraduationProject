package com.example.yzs.entity.chat;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.Entity;
import com.example.yzs.entity.chat.Answer;
import com.example.yzs.entity.chat.Offer;
import com.example.yzs.entity.chat.Candidate;


@Entity
@Data
public class Chat {
    private Integer id;
    private Integer uid;
    private String name;
    private String avatarUrl;
    private String role;
    private Integer fromId;
    private String fromName;
    private String fromAvatar;
    private String content;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    private java.sql.Timestamp time;
    private String type;
    private Integer toId;
    private String toName;
    private String toAvatar;
    private Integer readed;
    private Candidate candidate;
    private Offer offer;
    private Answer answer;

}
