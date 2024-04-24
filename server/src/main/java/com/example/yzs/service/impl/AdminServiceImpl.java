package com.example.yzs.service.impl;


import com.example.yzs.constant.MessageConstant;
import com.example.yzs.dao.AdminDao;
import com.example.yzs.entity.*;
import com.example.yzs.entity.shoppingcart.Orders;
import com.example.yzs.exception.CustomException;
import com.example.yzs.service.AdminService;
import com.example.yzs.utils.ValidatorUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class AdminServiceImpl implements AdminService {

    private final AdminDao adminDao;

    @Autowired
    public AdminServiceImpl(AdminDao adminDao) {
        this.adminDao = adminDao;
    }

    @Override
    public List<Account> finAll() {
        return adminDao.findAll();
    }

    @Override
    public Account login(Account account) {
        // 1.使用ValidatorUtil类进行登录校验x
        ValidatorUtil.loginVerify(account);

        //2．从数据库里面根据这个用户名和密码去查询对应的管理员信息，
        Account admin = adminDao.loginAdmin(account);


        if (admin == null) {
            //如果查出来没有,那说明输入的用户名或者密码有误，提示用户，不允许登录
            throw new CustomException(MessageConstant.LOGIN_FAIL);
        }
        //如果查出来了有,那说明确实有这个管理员，而且输入的用户名和密码都对
        return admin;
    }

    @Override
    public List<UserInfo> findAllUser() {
        return adminDao.findAllUserInfo();
    }

    @Override
    public PageBean<UserInfo> usersList(Integer currentPage,
                                        Integer pageSize,
                                        String selectCondition,
                                        Integer minAge,
                                        Integer maxAge) {
        //1,创建PageBean对象
        PageBean<UserInfo> pageBean = new PageBean<>();
        //2.开启分页查询PageHelp
        PageHelper.startPage(currentPage, pageSize);

        Map<String, Object> params = new HashMap<>();
        params.put("selectCondition", selectCondition);
        params.put("minAge", minAge);
        params.put("maxAge", maxAge);
        //3.调用mapper
        List<UserInfo> userInfoList = adminDao.usersList(params);
        //page中提供了方法，可以获取PageHelp分页查询后，得到的总记录条数和当前页数据
        Page<UserInfo> userInfoPage = (Page<UserInfo>) userInfoList;
        //把数据填充到PageBean对象中
        pageBean.setTotal(userInfoPage.getTotal());
        pageBean.setItems(userInfoPage.getResult());
        return pageBean;
    }

    @Override
    public void deleteUser(Integer uid, Integer deleted) {
        adminDao.deleteUser(uid, deleted);
        adminDao.deleteUserInfo(uid, deleted);
    }

    @Override
    public Account findId(Integer id) {
        return adminDao.findById(id);
    }

    @Override
    public void deleteConsultant(Integer cid, Integer deleted) {
        adminDao.deleteConsultant(cid, deleted);
        adminDao.deleteConsultantInfo(cid, deleted);
    }


    @Override
    public List<Books> findAllBook() {
        return adminDao.findAllBook();
    }

    @Override
    public void updateBooksInfo(Books books) {
        Set<String> ISBNsList = adminDao.selectISBN(); // 假设这是从数据库中查询到的ISBN集合
        String newISBN = books.getISBN();
        ValidatorUtil.booksInfoVerify(books);

        if (ISBNsList.contains(newISBN)) {
            if (books.getOldISBN().equals(newISBN)) {
                adminDao.updateBooksInfo(books);
            } else {
                throw new CustomException(MessageConstant.ISBN_IS_REPEAT);
            }
        } else {
            adminDao.updateBooksInfo(books);
        }

    }

    @Override
    public void addBooks(Books books) {
        ValidatorUtil.booksInfoVerify(books);
        adminDao.addBooks(books);
    }

    @Override
    public void delBooksInfo(Books books) {
        adminDao.delBooks(books);
    }

    @Override
    public List<Books> searchBookInfo(String search) {
        return adminDao.searchBookInfo(search);
    }

    @Override
    public  PageBean<Orders> findAllOrders(Integer currentPage, Integer pageSize, String selectName, String selectGoodsName) {
//1,创建PageBean对象
        PageBean<Orders> pageBean = new PageBean<>();
        //2.开启分页查询PageHelp
        PageHelper.startPage(currentPage, pageSize);
        //3.调用mapper
        List<Orders> ordersList = adminDao.findAllOrders(selectName, selectGoodsName);
        System.out.println(ordersList);
        //page中提供了方法，可以获取PageHelp分页查询后，得到的总记录条数和当前页数据
        Page<Orders> ordersPage = (Page<Orders>) ordersList;
        //把数据填充到PageBean对象中
        pageBean.setTotal(ordersPage.getTotal());
        pageBean.setItems(ordersPage.getResult());
        return pageBean;
    }

    @Override
    public void updateStatus(Orders orders) {
        adminDao.updateStatus(orders);
    }

    @Override
    public List<Type> findType() {
        return adminDao.findType();
    }

    @Override
    public List<Books> searchBookTypeInfo(String search) {
        return adminDao.searchBookTypeInfo(search);
    }

    @Override
    public Books findBookById(int id) {
        return adminDao.findBookById(id);
    }

    @Override
    public void saveBatch(List<Books> booksList) {
        System.out.println(booksList);
        adminDao.saveBatch(booksList);
    }


}
