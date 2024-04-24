package com.example.yzs.service.impl;

import com.example.yzs.dao.QuestionnaireDao;
import com.example.yzs.entity.Questionnaire;
import com.example.yzs.service.QuestionnaireService;
import com.example.yzs.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class QuestionnaireServiceImpl implements QuestionnaireService {

    private final QuestionnaireDao  questionnaireDao;

    @Autowired
    public QuestionnaireServiceImpl(QuestionnaireDao questionnaireDao) {
        this.questionnaireDao = questionnaireDao;
    }

    @Override
    public List<Questionnaire> questionnaire() {

        return questionnaireDao.questionnaire();
    }

    @Override
    public void answerQuestion(List<Questionnaire> questionnaire) {
        questionnaireDao.answerQuestion(questionnaire);
    }

    @Override
    public boolean surveyCompleted(Integer uid) {
        int count = questionnaireDao.surveyCompleted(uid);
        return count>0;
    }

    @Override
    public void typeScores(List<Questionnaire> questionnaires) {
        questionnaireDao.typeScores(questionnaires);
    }

    @Override
    public List<Questionnaire> echartsTypeScores() {
        return questionnaireDao.echartsTypeScores();
    }

}
