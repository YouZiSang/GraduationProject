package com.example.yzs.service.consultation;

import com.example.yzs.entity.consultation.ConsultationTable;
import com.example.yzs.entity.evaluation.ConsultationEvaluation;

import java.util.List;

public interface ConsultationService {
    void add(ConsultationTable consultation);

    List<ConsultationTable> findTable();

    void updateStatus(ConsultationTable consultationTable);

    void addEvaluation(ConsultationEvaluation consultationEvaluation);
}
