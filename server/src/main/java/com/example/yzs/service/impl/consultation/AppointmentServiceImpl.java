package com.example.yzs.service.impl.consultation;

import com.example.yzs.constant.MessageConstant;
import com.example.yzs.dao.consultation.AppointmentDao;
import com.example.yzs.entity.AppointmentSet;
import com.example.yzs.entity.consultation.AppointmentTable;
import com.example.yzs.exception.CustomException;
import com.example.yzs.service.consultation.AppointmentService;
import com.example.yzs.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentDao appointmentDao;

    @Autowired
    public AppointmentServiceImpl(AppointmentDao appointmentDao) {
        this.appointmentDao = appointmentDao;
    }


    @Override
    public void appointmentTable(AppointmentTable appointmentTable) {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer id = (Integer) map.get("uid");
        appointmentTable.setUid(id);
        appointmentDao.addTable(appointmentTable);
    }

    @Override
    public List<AppointmentTable> appointmentInfo(Integer id) {
        return appointmentDao.findAppointmentInfo(id);
    }

    @Override
    public void updateAppointment(AppointmentSet appointmentSet) {

        // 确保number不会变成负数
        if (appointmentSet.getNumber() > 0) {
            appointmentSet.setNumber(appointmentSet.getNumber() - 1);
            appointmentSet.setReservations(appointmentSet.getReservations() + 1);
            // 这里需要实现updateAppointmentSet方法，以更新数据库中的记录
            appointmentDao.updateAppointmentSet(appointmentSet);
        } else {
            throw new CustomException(MessageConstant.BOOKING_FAIL);
        }

    }

    @Override
    public List<AppointmentTable> findAppointmentTable() {
        return appointmentDao.findAppointmentTable();
    }

    @Override
    public void deleteAppointment(AppointmentTable appointment) {
        // 这里需要实现deleteAppointment方法，以删除数据库中的记录
        appointmentDao.deleteAppointment(appointment);
    }

    @Override
    public List<AppointmentTable> getAppTableListByCid(Integer cid) {

        return appointmentDao.getAppTableListByCid(cid);
    }

    @Override
    public List<AppointmentTable> getAppointmentNumber(Integer id) {
        return appointmentDao.getAppointmentNumber(id);
    }

    @Override
    public List<AppointmentTable> getAppTableListByUid(Integer uid) {
        return appointmentDao.getAppTableListByUid(uid);
    }

    @Override
    public void updateAppStatus(AppointmentTable appointmentTable) {
        appointmentDao.updateAppStatus(appointmentTable);
    }

    @Override
    public List<AppointmentTable> getAppointmentType() {
        return appointmentDao.getAppointmentType();
    }

    @Override
    public List<String> appointmentNumByCid() {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer cid = (Integer) map.get("cid");
        return appointmentDao.appointmentNumByCid(cid);
    }

    @Override
    public List<String> appointmentNumber() {
        return appointmentDao.appointmentNumber();
    }

}
