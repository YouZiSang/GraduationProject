package com.example.yzs.service.shoppingcart;

import com.example.yzs.entity.PageBean;
import com.example.yzs.entity.evaluation.BookEvaluation;
import com.example.yzs.entity.shoppingcart.Orders;
import com.example.yzs.entity.shoppingcart.ShoppingCart;

import java.util.List;

public interface OrdersService {
    void buyBooks(Integer shopperId, List<ShoppingCart> selectedItems);
    

    Orders selectByOrderNo(String orderNo);

    void updateById(Orders orders);

    PageBean<Orders> selectOrders(Integer buyerId, Integer currentPage, Integer pageSize, String selectCondition);

    void updateStatus(Orders orders);

    void evaluation(BookEvaluation evaluation, Integer evaId);

    void delOrder(String orderNo);

    List<String> salesEcharts();
}
