package com.example.yzs.dao.shoppingcart;

import com.example.yzs.entity.shoppingcart.ShoppingCart;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface ShoppingCartDao {
    ShoppingCart findByISBN(@Param("ISBN") String ISBN, @Param("shopper_id") Integer shopperId);

    void addProducts(ShoppingCart shoppingCart);

    void updateShoppingCart(ShoppingCart shoppingCart);

    List<ShoppingCart> selectShoppingCartList(Integer shopperId);

    void batchDelete(@Param("shopperId") Integer shopperId, @Param("selectedItems") List<ShoppingCart> selectedItems);

    void delete(@Param("shopperId")Integer shopperId,  @Param("ISBN")String ISBN);

    Map<String, Object> selectBookNumber(Integer shopperId);

    void updateNumber(ShoppingCart shoppingCart);

    void update(ShoppingCart shoppingCart);
}
