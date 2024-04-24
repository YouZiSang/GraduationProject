package com.example.yzs.service;

import com.example.yzs.entity.*;

import java.util.List;

public interface ConsultantsService {

     //条件分页列表查询
     PageBean<ConsultantsInfo> list(Integer currentPage, Integer pageSize, String selectName, Integer minAge, Integer maxAge);


    Account login(Account account);

    AppointmentSet addAppointment(AppointmentSet appointmentSet);

    List<AppointmentSet> findApSet(Integer id);
}
