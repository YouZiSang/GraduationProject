package com.example.yzs.entity.consultation;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.Entity;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Entity
public class ConsultationTable {
    private Integer id;
    private Integer uid;
    private String avatarUrl;
    private String nickname;
    private String name;
    private String type;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDate consultationDate;
    private String startTime;
    private String endTime;
    private String description;
    private String status;
    private Integer cid;
}
