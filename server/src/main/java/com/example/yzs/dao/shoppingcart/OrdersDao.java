package com.example.yzs.dao.shoppingcart;

import com.example.yzs.entity.evaluation.BookEvaluation;
import com.example.yzs.entity.shoppingcart.Orders;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OrdersDao {

    void addOrders(@Param("orders") Orders orders, @Param("expireAt") String expireAt);
    List<Orders> selectOrders(@Param("buyerId") Integer buyerId, @Param("selectCondition") String selectCondition);

    Orders selectByOrdersNo(String orderNo);

    void updateById(Orders orders);

    void updateStatus(Orders orders);

    void evaluationOrder(@Param("evaluation") BookEvaluation evaluation, @Param("mediaString") String mediaString, @Param("evaId") Integer evaId);

    void delOrder(String orderNo);

    List<String> salesEcharts();
}
