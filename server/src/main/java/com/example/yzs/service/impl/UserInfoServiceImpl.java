package com.example.yzs.service.impl;

import com.example.yzs.dao.UserInfoDao;
import com.example.yzs.entity.ConsultantsInfo;
import com.example.yzs.entity.UserInfo;
import com.example.yzs.service.UserInfoService;
import com.example.yzs.utils.ValidatorUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class UserInfoServiceImpl implements UserInfoService {
    @Autowired
    private UserInfoDao userInfoDao;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public UserInfo findById(Integer id){
        return userInfoDao.findById(id);
    }

    @Override
    public void add(UserInfo userInfo) {
        ValidatorUtil.phoneAndSexVerify(userInfo);
        userInfoDao.insertUserInfo(userInfo);
    }

    @Override
    public UserInfo findAvatar(Integer uid) {
        UserInfo userInfo = userInfoDao.findAvatar(uid);
        return userInfo;
    }

    @Override
    public void edit(UserInfo userInfo) {

        ValidatorUtil.phoneAndSexVerify(userInfo);
        ValidatorUtil.nicknameAndEmailVerify(userInfo);
        userInfoDao.editPersonalInfo(userInfo);
    }

    @Override
    public List<UserInfo> getUsersList() {
        return userInfoDao.getUsersList();
    }

    @Override
    public void upGrade(ConsultantsInfo consultantsInfo) {
        ValueOperations<String, Object> operations = redisTemplate.opsForValue();
        operations.set("upGradeInfo:" + consultantsInfo.getUid(), consultantsInfo, 12, TimeUnit.HOURS);
    }

    @Override
    public ConsultantsInfo statusUpGrade(Integer id) {
        ValueOperations<String, Object> operations = redisTemplate.opsForValue();
        ConsultantsInfo storedInfo = (ConsultantsInfo) operations.get("upGradeInfo:" + id);
        return storedInfo;
    }

}
