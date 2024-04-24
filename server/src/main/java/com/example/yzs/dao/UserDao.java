package com.example.yzs.dao;

import com.example.yzs.entity.Account;
import com.example.yzs.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserDao {

    Account login(Account account);

    @Select("select * from account where uid = #{uid} and password = #{password}")
    Account selectByPrimaryKey(Integer uid);

    void insertUser(Account account);

    void updatePwd(@Param("uid") Integer uid, @Param("newPwd") String newPwd);
}
