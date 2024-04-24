package com.example.yzs.entity.evaluation;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.util.Date;


@Data
public class BookEvaluation {
    private Integer id;
    private String orderNo;
    private String name;
    private String avatar;
    private Integer goodsId;
    private Integer evaId;
    private String content;
    private String media;
    private String description;
    private String logistics;
    private String attitude;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createdTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updatedTime;
}
