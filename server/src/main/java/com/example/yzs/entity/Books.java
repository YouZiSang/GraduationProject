package com.example.yzs.entity;

import cn.hutool.core.annotation.Alias;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
@Data
public class Books {
    private Integer id;
    private Integer collectorsId;
    private String oldISBN;
    @JsonProperty("ISBN")
    private String ISBN;
    @Alias("书名")
    private String title;
    @Alias("封面")
    private String image;
    @Alias("作者")
    private String author;
    @Alias("出版社")
    private String publisher;
    @Alias("数量")
    private Integer number;
    @Alias("图书类型")
    private String bookType;
    @Alias("原价")
    @Column(name = "oldPrice")
    private BigDecimal oldPrice;
    @Alias("现价")
    private BigDecimal price;
    private Long collectNumber;
    @Alias("销售量")
    private Integer sales;
    private String role;
    private String createdTime;
    private String updatedTime;
}
