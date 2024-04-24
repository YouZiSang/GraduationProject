package com.example.yzs.entity.exchange;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

@Data
public class Comment {
    private Integer id;
    private Integer landlordId;
    //点赞id
    private Integer thumbsUpId;
    //评论id
    private Integer commentId;
    private String avatar;
    private String name;
    private String content;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private java.sql.Timestamp createdTime;
    //被点赞，评论的id
    private Integer messageId;
    //回复那个人的id
    private Integer replyId;
    //点赞回复评论的id
    private Integer commentReplyId;
    private String role;
    private Integer likeCount;
    private Integer commentCount;
}
