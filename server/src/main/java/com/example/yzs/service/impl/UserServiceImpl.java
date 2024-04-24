package com.example.yzs.service.impl;


import com.example.yzs.constant.MessageConstant;
import com.example.yzs.dao.UserDao;
import com.example.yzs.dao.chat.ChatDao;
import com.example.yzs.entity.Account;
import com.example.yzs.entity.Questionnaire;
import com.example.yzs.exception.CustomException;
import com.example.yzs.service.UserService;
import com.example.yzs.utils.Md5Util;
import com.example.yzs.utils.ValidatorUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserDao userDao;
    private final ChatDao chatDao;
    @Autowired
    public UserServiceImpl(UserDao userDao, ChatDao chatDao) {
        this.userDao = userDao;
        this.chatDao = chatDao;
    }

    @Override
    public Account login(Account account) {
        // 1.使用ValidatorUtil类进行登录校验x
        ValidatorUtil.loginVerify(account);

        //2．从数据库里面根据这个用户名和密码去查询对应的管理员信息，
        account = userDao.login(account);

        if (account == null) {
            //如果查出来没有,那说明输入的用户名或者密码有误，提示用户，不允许登录
            throw new CustomException(MessageConstant.LOGIN_FAIL);
        }
        if (account.getDeleted() == 1) {
            throw new CustomException(MessageConstant.ACCOUNT_DELETED);
        }

        return account;
    }

    public Account findById(Integer id) {
        return userDao.selectByPrimaryKey(id);
    }

    @Override
    public void add(Account account) {
        /**
         * 判断密码是否为空
         * 是否 8-16 位
         * 是否包含数字和字母
         * */
        ValidatorUtil.registerPasswordVerify(account);
        //密码加密
        String md5String = Md5Util.getMD5String(account.getPassword());
        account.setPassword(md5String);
        userDao.insertUser(account);
    }


    @Override
    public void updatePwd(Integer uid,String newPwd) {
        userDao.updatePwd(uid,Md5Util.getMD5String(newPwd));
    }



}
