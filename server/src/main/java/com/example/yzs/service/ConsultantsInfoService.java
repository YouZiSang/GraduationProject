package com.example.yzs.service;

import com.example.yzs.entity.ConsultantsInfo;
import com.example.yzs.entity.evaluation.ConsultationEvaluation;

import java.util.List;

public interface ConsultantsInfoService {
    List<ConsultantsInfo> consultantsInfoList();

    ConsultantsInfo personalInfo(Integer id);

    void updateInfo(ConsultantsInfo consultantsInfo);

    ConsultantsInfo getStatusEdit(Integer id);

    List<ConsultationEvaluation> evaluation(Integer cid);
}
