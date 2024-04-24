package com.example.yzs.service.impl;

import com.example.yzs.dao.AuditDao;
import com.example.yzs.entity.ConsultantsInfo;
import com.example.yzs.entity.UserInfo;
import com.example.yzs.service.AuditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class AuditServiceImpl implements AuditService {

    @Autowired
    private  RedisTemplate<String, Object> redisTemplate;

    private final AuditDao auditDao;

    @Autowired
    public AuditServiceImpl(AuditDao auditDao) {
        this.auditDao = auditDao;
    }


    @Override
    public List<ConsultantsInfo> auditConsultants() {
        Map<String, ConsultantsInfo> resultMap = new HashMap<>();
        ScanOptions options = ScanOptions.scanOptions().match("updateInfo:*").build();
        try (Cursor<byte[]> cursor = (Cursor) redisTemplate.executeWithStickyConnection(redisConnection ->
                redisConnection.scan(options))) {
            while (cursor.hasNext()) {
                String key = new String(cursor.next());
                ConsultantsInfo value = (ConsultantsInfo) redisTemplate.opsForValue().get(key);
                if (value != null) {
                    resultMap.put(key, value);
                }
            }
        } catch (Exception e) {
            // 处理异常
            e.printStackTrace(); // 打印堆栈跟踪以便于调试
        }

        // 返回值集合，指定泛型参数为ConsultantsInfo
        return new ArrayList<>(resultMap.values());
    }

    @Override
    public List<ConsultantsInfo> findEditConsultants() {
        return auditDao.findEditConsultants();
    }

    @Override
    public void successEdit(ConsultantsInfo consultantsInfo) {
        ValueOperations<String, Object> operations = redisTemplate.opsForValue();
        operations.set("updateInfo:" + consultantsInfo.getCid(), consultantsInfo, 12, TimeUnit.HOURS);
        auditDao.successEdit(consultantsInfo);
    }

    @Override
    public void failEdit(ConsultantsInfo consultantsInfo) {
        ValueOperations<String, Object> operations = redisTemplate.opsForValue();
        operations.set("updateInfo:" + consultantsInfo.getCid(), consultantsInfo, 12, TimeUnit.HOURS);
    }

    @Override
    public List<ConsultantsInfo> findUpGrade() {
        Map<String, ConsultantsInfo> resultMap = new HashMap<>();
        ScanOptions options = ScanOptions.scanOptions().match("upGradeInfo:*").build();
        try (Cursor<byte[]> cursor = (Cursor) redisTemplate.executeWithStickyConnection(redisConnection ->
                redisConnection.scan(options))) {
            while (cursor.hasNext()) {
                String key = new String(cursor.next());
                ConsultantsInfo value = (ConsultantsInfo) redisTemplate.opsForValue().get(key);
                if (value != null) {
                    resultMap.put(key, value);
                }
            }
        } catch (Exception e) {
            // 处理异常
            e.printStackTrace(); // 打印堆栈跟踪以便于调试
        }

        // 返回值集合，指定泛型参数为ConsultantsInfo
        return new ArrayList<>(resultMap.values());
    }

    @Override
    public List<UserInfo> findUserInfo() {
        return auditDao.findUserInfo();
    }

    @Override
    public void successUpGrade(ConsultantsInfo consultantsInfo) {
        ValueOperations<String, Object> operations = redisTemplate.opsForValue();
        operations.set("upGradeInfo:" + consultantsInfo.getCid(), consultantsInfo, 12, TimeUnit.HOURS);
        auditDao.updateAccountType(consultantsInfo);
        auditDao.deleteUserInfo(consultantsInfo);
        consultantsInfo.setCid(consultantsInfo.getUid());
        auditDao.addConsultants(consultantsInfo);
    }

    @Override
    public void failUpGrade(ConsultantsInfo consultantsInfo) {
        ValueOperations<String, Object> operations = redisTemplate.opsForValue();
        operations.set("upGradeInfo:" + consultantsInfo.getUid(), consultantsInfo, 12, TimeUnit.HOURS);
    }
}
