package com.example.yzs.entity.exchange;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;


@Data
public class Community {
    private Integer landlordId;
    private Integer messageId;
    private String avatar;
    private String name;
    private String content;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private java.sql.Timestamp createdTime;
    private String role;
    private Integer likeCount;
    private Integer commentCount;

}
