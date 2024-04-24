package com.example.yzs.service.consultation;

import com.example.yzs.entity.AppointmentSet;
import com.example.yzs.entity.consultation.AppointmentTable;

import java.util.List;

public interface AppointmentService {
    void appointmentTable(AppointmentTable appointmentTable);

    List<AppointmentTable> appointmentInfo(Integer id);

    void updateAppointment(AppointmentSet appointmentSet);

    List<AppointmentTable> findAppointmentTable();

    void deleteAppointment(AppointmentTable appointment);

    List<AppointmentTable> getAppTableListByCid(Integer cid);

    List<AppointmentTable> getAppointmentNumber(Integer id);

    List<AppointmentTable> getAppTableListByUid(Integer uid);

    void updateAppStatus(AppointmentTable appointmentTable);

    List<AppointmentTable> getAppointmentType();

    List<String> appointmentNumByCid();

    List<String> appointmentNumber();
}
