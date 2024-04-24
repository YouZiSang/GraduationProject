package com.example.yzs.controller;

import com.example.yzs.constant.MessageConstant;
import com.example.yzs.constant.Result;
import com.example.yzs.entity.Questionnaire;
import com.example.yzs.service.QuestionnaireService;
import com.example.yzs.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RequestMapping()
@RestController
public class QuestionnaireController {

    private final QuestionnaireService questionnaireService;

    @Autowired
    public QuestionnaireController(QuestionnaireService questionnaireService) {
        this.questionnaireService = questionnaireService;
    }

    @GetMapping("questionnaires")
    public Result<List<Questionnaire>> listQuestionnaire() {

        return Result.success(questionnaireService.questionnaire());
    }

    @PostMapping("answerQuestion")
    public Result<String> answerQuestion(@RequestBody List<Questionnaire> questionnaireList) {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer uid = (Integer) map.get("uid");
        for (Questionnaire questionnaire : questionnaireList) {
            questionnaire.setUid(uid);
        }

        questionnaireService.answerQuestion(questionnaireList);
        return Result.success(MessageConstant.SUBMIT_QUESTIONNAIRE_SUCCESS);
    }

    @GetMapping("surveyCompleted")
    public Result<Object> surveyCompleted() {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer uid = (Integer) map.get("uid");
        boolean isCompleted = questionnaireService.surveyCompleted(uid);

        if (isCompleted) {
            return Result.success(MessageConstant.QUESTIONNAIRE_COMPLETED, true);
        } else {
            return Result.success(MessageConstant.QUESTIONNAIRE_NOT_COMPLETED, false);
        }
    }

    @PostMapping("answerScore")
    public Result<String> answerScore(@RequestBody Map<Integer, Integer> typeScores) {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer uid = (Integer) map.get("uid");

        List<Questionnaire> questionnaires = new ArrayList<>();
        for (Map.Entry<Integer, Integer> entry : typeScores.entrySet()) {
            Integer type = entry.getKey();
            Integer score = entry.getValue();

            Questionnaire questionnaire = new Questionnaire();
            questionnaire.setUid(uid);
            questionnaire.setQuestionType(type);
            questionnaire.setTypeScores(score);
            questionnaires.add(questionnaire);
        }

        questionnaireService.typeScores(questionnaires);

        return Result.success("回答成功");
    }

}
