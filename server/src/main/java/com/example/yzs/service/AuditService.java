package com.example.yzs.service;

import com.example.yzs.entity.ConsultantsInfo;
import com.example.yzs.entity.UserInfo;

import java.util.List;

public interface AuditService {
    List<ConsultantsInfo> auditConsultants();

    List<ConsultantsInfo> findEditConsultants();

    void successEdit(ConsultantsInfo consultantsInfo);

    void failEdit(ConsultantsInfo consultantsInfo);

    List<ConsultantsInfo> findUpGrade();

    List<UserInfo> findUserInfo();

    void successUpGrade(ConsultantsInfo consultantsInfo);

    void failUpGrade(ConsultantsInfo consultantsInfo);
}
