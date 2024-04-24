package com.example.yzs.entity;

import lombok.Data;

import javax.persistence.Entity;
import java.util.Map;

@Entity
@Data
public class Questionnaire {
    private Integer id;
    private String questionText;
    private Integer typeScores;
    private Integer uid;
    private Integer questionType;
    private String typeName;
    private String options;
    private String optionText;
    private String answerOption;
    private Integer answerId;
    private Integer questionId;
}
