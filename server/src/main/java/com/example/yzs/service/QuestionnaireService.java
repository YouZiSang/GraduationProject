package com.example.yzs.service;

import com.example.yzs.entity.Questionnaire;

import java.util.List;

public interface QuestionnaireService {
    List<Questionnaire> questionnaire();

    void answerQuestion(List<Questionnaire> questionnaire);

    boolean surveyCompleted(Integer uid);

    void typeScores(List<Questionnaire> questionnaires);

    List<Questionnaire> echartsTypeScores();


}
