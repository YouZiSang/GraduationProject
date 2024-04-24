package com.example.yzs.dao;

import com.example.yzs.entity.Account;
import com.example.yzs.entity.AppointmentSet;
import com.example.yzs.entity.Consultants;
import com.example.yzs.entity.ConsultantsInfo;
import com.example.yzs.entity.evaluation.ConsultationEvaluation;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Vector;

@Mapper
public interface ConsultantsDao {

     Account login(Account account);

     List<ConsultantsInfo> getConsultantsInfoList();

     List<ConsultantsInfo> list(@Param("name") String selectName, @Param("minAge") Integer minAge, @Param("maxAge") Integer maxAge);

//     @Insert("insert into c_appointment_set values (#{id},#{consultantsId},#{appointmentDate},#{number},#{reservations})")
     void addAppointment(AppointmentSet appointmentSet);

     List<AppointmentSet> findApSet(Integer id);

}
