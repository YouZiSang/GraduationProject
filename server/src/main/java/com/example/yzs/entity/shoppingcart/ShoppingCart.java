package com.example.yzs.entity.shoppingcart;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.Entity;
import java.math.BigDecimal;

@Entity
@Data
public class ShoppingCart {
    private Integer id;
    private Integer shopperId;
    private Integer goodsId;
    private String image;
    private String title;
    @JsonProperty("ISBN")
    private String ISBN;
    private String author;
    private BigDecimal price;
    private Integer number;
    private BigDecimal totalPrice;
    private String deliveryAdd;
}
