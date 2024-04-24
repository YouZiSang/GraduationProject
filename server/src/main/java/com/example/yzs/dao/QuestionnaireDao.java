package com.example.yzs.dao;

import com.example.yzs.entity.Questionnaire;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface QuestionnaireDao {

    List<Questionnaire> questionnaire();

    void answerQuestion(List<Questionnaire> questionnaire);

    int surveyCompleted(Integer uid);

    void typeScores(List<Questionnaire> questionnaires);

    List<Questionnaire> echartsTypeScores();
}
