package com.example.yzs.dao;

import com.example.yzs.entity.ConsultantsInfo;
import com.example.yzs.entity.UserInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AuditDao {
    List<ConsultantsInfo> findEditConsultants();

    void successEdit(ConsultantsInfo consultantsInfo);

    List<UserInfo> findUserInfo();

    void addConsultants(ConsultantsInfo consultantsInfo);

    void deleteUserInfo(ConsultantsInfo consultantsInfo);

    void updateAccountType(ConsultantsInfo consultantsInfo);
}
