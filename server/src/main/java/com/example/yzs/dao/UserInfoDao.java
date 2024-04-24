package com.example.yzs.dao;

import com.example.yzs.entity.UserInfo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserInfoDao {
    UserInfo findById(Integer id);

    @Insert("insert into user_info(uid,avatarUrl,sex,nickname,phone) values (#{uid},#{avatarUrl},#{sex},#{nickname},#{phone})")
    void insertUserInfo(UserInfo userInfo);

    @Select("select avatarUrl,nickname from user_info where uid =#{uid}")
    UserInfo findAvatar(@Param("uid") Integer uid);

    void editPersonalInfo(UserInfo userInfo);

    List<UserInfo> getUsersList();
}
