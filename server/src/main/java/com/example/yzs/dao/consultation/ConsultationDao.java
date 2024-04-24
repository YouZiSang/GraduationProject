package com.example.yzs.dao.consultation;

import com.example.yzs.entity.consultation.ConsultationTable;
import com.example.yzs.entity.evaluation.ConsultationEvaluation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ConsultationDao {

    void add(ConsultationTable consultation);
    List<ConsultationTable> findConsultationTable();

    void updateStatus(ConsultationTable consultationTable);

    void evaluationConsultants(@Param("consultationEvaluation") ConsultationEvaluation consultationEvaluation, @Param("mediaString") String mediaString);
}
