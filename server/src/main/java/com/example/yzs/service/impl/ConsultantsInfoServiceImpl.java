package com.example.yzs.service.impl;

import com.example.yzs.dao.ConsultantsDao;
import com.example.yzs.dao.consultants.ConsultantsInfoDao;
import com.example.yzs.entity.ConsultantsInfo;
import com.example.yzs.entity.evaluation.ConsultationEvaluation;
import com.example.yzs.service.ConsultantsInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
public class ConsultantsInfoServiceImpl implements ConsultantsInfoService {

    private final ConsultantsDao consultantsDao;
    private final ConsultantsInfoDao consultantsInfoDao;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public ConsultantsInfoServiceImpl(ConsultantsDao consultantsDao, ConsultantsInfoDao consultantsInfoDao) {
        this.consultantsDao = consultantsDao;
        this.consultantsInfoDao = consultantsInfoDao;
    }

    @Override
    public List<ConsultantsInfo> consultantsInfoList() {
        return consultantsDao.getConsultantsInfoList();
    }

    @Override
    public ConsultantsInfo personalInfo(Integer id) {
        return consultantsInfoDao.getPersonalInfo(id);
    }

    @Override
    public void updateInfo(ConsultantsInfo consultantsInfo) {
        ValueOperations<String, Object> operations = redisTemplate.opsForValue();
        operations.set("updateInfo:" + consultantsInfo.getCid(), consultantsInfo, 12, TimeUnit.HOURS);
    }

    @Override
    public ConsultantsInfo getStatusEdit(Integer id) {
        ValueOperations<String, Object> operations = redisTemplate.opsForValue();
        ConsultantsInfo storedInfo = (ConsultantsInfo) operations.get("updateInfo:" + id);
        return storedInfo;
    }

    @Override
    public List<ConsultationEvaluation> evaluation(Integer cid) {
        return consultantsInfoDao.evaluate(cid);
    }
}
