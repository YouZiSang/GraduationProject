package com.example.yzs.service;


import com.example.yzs.entity.ConsultantsInfo;
import com.example.yzs.entity.UserInfo;

import java.util.List;

public interface UserInfoService {
    UserInfo findById(Integer id);

    void add(UserInfo userInfo);

    UserInfo findAvatar(Integer id);

    void edit(UserInfo userInfo);

    List<UserInfo> getUsersList();

    void upGrade(ConsultantsInfo consultantsInfo);

    ConsultantsInfo statusUpGrade(Integer id);
}
