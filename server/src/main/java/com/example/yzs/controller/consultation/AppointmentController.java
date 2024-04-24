package com.example.yzs.controller.consultation;

import cn.hutool.core.collection.CollectionUtil;
import com.example.yzs.constant.MessageConstant;
import com.example.yzs.constant.Result;
import com.example.yzs.entity.AppointmentSet;
import com.example.yzs.entity.consultation.AppointmentTable;
import com.example.yzs.service.consultation.AppointmentService;
import com.example.yzs.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@RestController
public class AppointmentController {
    private final AppointmentService appointmentService;

    @Autowired
    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @PostMapping("/user/appointment")
    public Result<List<AppointmentTable>> appointmentTable(@RequestBody AppointmentTable appointmentTable) {
        appointmentService.appointmentTable(appointmentTable);
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer id = (Integer) map.get("uid");
        List<AppointmentTable> table = appointmentService.appointmentInfo(appointmentTable.getUid());
        return Result.success(table);
    }

    @GetMapping("/user/appointmentInfo")
    public Result<List<AppointmentTable>> appointmentInfo() {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer id = (Integer) map.get("uid");
        List<AppointmentTable> appointmentTableList = appointmentService.appointmentInfo(id);
        return Result.success(appointmentTableList);
    }

    @PostMapping("/user/updateAppointmentInfo")
    public Result<String> updateAppointment(@RequestBody AppointmentSet appointmentSet) {
        appointmentService.updateAppointment(appointmentSet);
        return Result.success("预约成功！");
    }

//    @Scheduled(cron = "0 0 0 * * ?")// 每天凌晨执行一次
@Scheduled(fixedRate = 60000) // 每分钟执行一次
    public void deleteAppointments() {
        List<AppointmentTable> appointments = appointmentService.findAppointmentTable();
        LocalDate fifteenDaysAgo = LocalDate.now().minusDays(15); // 计算15天前的日期
        for (AppointmentTable appointment : appointments) {
            // 检查预约日期是否在15天前
            if (appointment.getAppointmentDate().isBefore(fifteenDaysAgo)) {
                // 如果预约日期在15天前，则删除该预约记录
                appointmentService.deleteAppointment(appointment);
            } else if (appointment.getAppointmentDate().isEqual(LocalDate.now())) {
                // 如果预约日期是今天，并且状态为 "成功预约"，则将状态改为 "进行中"
                if ("成功预约".equals(appointment.getStatus())) {
                    appointment.setStatus("进行中");
                    appointmentService.updateAppStatus(appointment);
                }
            }
        }
    }

    @GetMapping("getAppTableByCid")
    public Result<List<AppointmentTable>> getAppTableByCid() {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer cid = (Integer) map.get("cid");
        List<AppointmentTable> appointmentTableList = appointmentService.getAppTableListByCid(cid);
        return Result.success(appointmentTableList);
    }

    @GetMapping("getAppointmentNumber")
    public Result<List<Map<String, Object>>> getAppointmentNumber() {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer id = (Integer) map.get("cid");
        List<AppointmentTable> appointmentNumber = appointmentService.getAppointmentNumber(id);
        Map<LocalDate, Long> collect = appointmentNumber.stream()
                .filter(x -> CollectionUtil.isNotEmpty(Collections.singleton(x.getAppointmentDate())))
                .collect(Collectors.groupingBy(AppointmentTable::getAppointmentDate, Collectors.counting()));

        List<Map<String, Object>> mapList = new ArrayList<>();
        for (LocalDate key : collect.keySet()) {
            Map<String, Object> numberMap = new java.util.HashMap<>();
            numberMap.put("date", key);
            numberMap.put("number", collect.get(key));
            mapList.add(numberMap);
        }
        return Result.success(mapList);
    }

    @GetMapping("getAppTableByUid")
    public Result<List<AppointmentTable>> getAppByUid() {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer uid = (Integer) map.get("uid");
        List<AppointmentTable> appointmentTableList = appointmentService.getAppTableListByUid(uid);
        return Result.success(appointmentTableList);
    }

    @PostMapping("updateAppStatus")
    public Result<String> updateAppStatus(@RequestBody AppointmentTable appointmentTable) {

        appointmentService.updateAppStatus(appointmentTable);
        return Result.success(MessageConstant.CONTROLLER_SUCCESS);
    }

    @GetMapping("/getAppointmentType")
    public Result<List<AppointmentTable>> getAppType() {
        List<AppointmentTable> appointmentType = appointmentService.getAppointmentType();
        return Result.success(appointmentType);
    }
}
