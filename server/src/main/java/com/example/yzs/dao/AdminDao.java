package com.example.yzs.dao;


import com.example.yzs.entity.*;
import com.example.yzs.entity.shoppingcart.Orders;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Mapper
public interface AdminDao {
    List<Account> findAll();

    @Select("select * from account where uid = #{uid} and password = #{password}")
    Account loginAdmin(Account account);

    @Select("select * from user_info")
    List<UserInfo> findAllUserInfo();

    List<UserInfo> usersList(Map<String, Object> params);

    void deleteUser(@Param("uid")Integer uid,@Param("deleted")Integer deleted);

    void deleteUserInfo(@Param("uid")Integer uid,@Param("deleted")Integer deleted);

    @Select("select * from account where uid = #{uid} ")
    Account findById(Integer id);

    void deleteConsultant(@Param("cid")Integer cid,@Param("deleted")Integer deleted);

    void deleteConsultantInfo(@Param("cid")Integer cid,@Param("deleted")Integer deleted);


    List<Books> findAllBook();

    Set<String> selectISBN();

    void updateBooksInfo(Books books);

    void addBooks(Books books);

    void delBooks(Books books);

    List<Books> searchBookInfo(String search);

    List<Orders> findAllOrders(@Param("selectName") String selectName, @Param("selectGoodsName") String selectGoodsName);

    void updateStatus(Orders orders);

    List<Type> findType();

    List<Books> searchBookTypeInfo(String search);

    Books findBookById(int id);

    void saveBatch(List<Books> booksList);
}
