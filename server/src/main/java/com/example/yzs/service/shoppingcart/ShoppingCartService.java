package com.example.yzs.service.shoppingcart;

import com.example.yzs.entity.PageBean;
import com.example.yzs.entity.shoppingcart.ShoppingCart;

import java.util.List;
import java.util.Map;

public interface ShoppingCartService {
    void addProducts(ShoppingCart shoppingCart,Integer shopperId);

    List<ShoppingCart> shoppingCartList(Integer shopperId);

    void batchDeleteBooks(Integer shopperId, List<ShoppingCart> selectedItems);

    void deleteBooks(Integer shopperId, String ISBN);

    Map<String, Object> selectBooksNumber(Integer shopperId);

    void updateBooksNumber(ShoppingCart shoppingCart,Integer shopperId);

}
