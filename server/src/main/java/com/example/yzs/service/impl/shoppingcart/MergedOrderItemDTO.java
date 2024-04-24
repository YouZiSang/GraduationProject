package com.example.yzs.service.impl.shoppingcart;

import lombok.Data;

@Data
public class MergedOrderItemDTO {
    private String goodsName;
    private Integer number;
    private Double totalPrice;
}
