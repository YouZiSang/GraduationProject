package com.example.yzs.entity.shoppingcart;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.security.Timestamp;
import java.util.Date;

@Data
public class Orders {
    private Integer id;
    private String orderNo;
    private Integer goodsId;
    private String goodsName;
    private Integer number;
    private BigDecimal totalPrice;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private java.sql.Timestamp createdTime;
    private String payNo;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private java.sql.Timestamp payTime;
    private String payStatus;
    private String status;
    private Integer buyerId;
    private String buyerName;
    private String deliveryAdd;
}
