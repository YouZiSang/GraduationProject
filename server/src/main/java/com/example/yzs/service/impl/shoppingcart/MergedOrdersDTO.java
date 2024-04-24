package com.example.yzs.service.impl.shoppingcart;

import lombok.Data;

import java.util.List;

@Data
public class MergedOrdersDTO {
    private Integer id;
    private String orderNo;
    private String createdTime;
    private String payNo;
    private String payTime;
    private String status;
    private List<MergedOrderItemDTO> items;
}
