package com.example.yzs.entity.evaluation;

import lombok.Data;

import javax.persistence.Entity;

@Data
@Entity
public class ConsultationEvaluation {
    private Integer id;
    private Integer evaId;
    private String name;
    private String avatar;
    private String consultationDate;
    private String content;
    private String media;
    private String attitude;
    private String professional;
    private String reliable;
    private String createdTime;
    private Integer cid;
}
