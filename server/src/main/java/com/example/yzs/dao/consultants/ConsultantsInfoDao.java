package com.example.yzs.dao.consultants;

import com.example.yzs.entity.ConsultantsInfo;
import com.example.yzs.entity.evaluation.ConsultationEvaluation;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ConsultantsInfoDao {
    ConsultantsInfo getPersonalInfo(Integer id);

    List<ConsultationEvaluation> evaluate(Integer cid);
}
