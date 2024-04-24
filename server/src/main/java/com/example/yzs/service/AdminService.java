package com.example.yzs.service;


import com.example.yzs.entity.*;
import com.example.yzs.entity.shoppingcart.Orders;

import java.util.List;

public interface AdminService {
    List<Account> finAll();

    Account login(Account account);

    List<UserInfo> findAllUser();

    PageBean<UserInfo> usersList(Integer currentPage, Integer pageSize, String selectCondition, Integer minAge, Integer maxAge);

    void deleteUser(Integer uid, Integer deleted);

    Account findId(Integer id);

    void deleteConsultant(Integer cid, Integer deleted);

    List<Books> findAllBook();

    void updateBooksInfo(Books books);


    void addBooks(Books books);

    void delBooksInfo(Books books);

    List<Books> searchBookInfo(String search);

    PageBean<Orders> findAllOrders(Integer currentPage, Integer pageSize, String selectName,String selectGoodsName);

    void updateStatus(Orders orders);

    List<Type> findType();

    List<Books> searchBookTypeInfo(String search);

    Books findBookById(int id);

    void saveBatch(List<Books> booksList);
}
