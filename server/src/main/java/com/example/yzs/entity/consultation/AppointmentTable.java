package com.example.yzs.entity.consultation;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentTable {
    private Integer id;
    private Integer cid;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDate appointmentDate;
    private String status;
    private Integer uid;
    private String name;    //姓名
    private String avatarUrl;  //头像
    private String nickname;  //昵称
    private String sex;     //性别
    private Integer age;        //年龄
    private String email;
    private String phone;
    private String appointmentType;
}
