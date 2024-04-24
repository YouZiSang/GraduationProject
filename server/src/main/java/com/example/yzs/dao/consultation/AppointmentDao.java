package com.example.yzs.dao.consultation;

import com.example.yzs.entity.AppointmentSet;
import com.example.yzs.entity.consultation.AppointmentTable;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AppointmentDao {

    void addTable(AppointmentTable appointmentTable);

    List<AppointmentTable> findAppointmentInfo(Integer id);

    void updateAppointmentSet(AppointmentSet appointmentSet);
    
    List<AppointmentTable> findAppointmentTable();

    void deleteAppointment(AppointmentTable appointment);

    List<AppointmentTable> getAppTableListByCid(Integer cid);

    List<AppointmentTable> getAppointmentNumber(Integer cid);

    List<AppointmentTable> getAppTableListByUid(Integer uid);

    void updateAppStatus(AppointmentTable appointmentTable);

    List<AppointmentTable> getAppointmentType();

    List<String> appointmentNumByCid(Integer cid);

    List<String> appointmentNumber();
}
