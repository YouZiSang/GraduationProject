package com.example.yzs.controller;

import com.example.yzs.constant.MessageConstant;
import com.example.yzs.constant.Result;
import com.example.yzs.entity.ConsultantsInfo;
import com.example.yzs.entity.UserInfo;
import com.example.yzs.service.AuditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping
public class AuditController {

    private final AuditService auditService;
    @Autowired
    public AuditController(AuditService auditService) {
        this.auditService = auditService;
    }
    @GetMapping("/audit")
    public Result<List<ConsultantsInfo>> audit() {
        List<ConsultantsInfo> list = auditService.auditConsultants();
        return Result.success((List<ConsultantsInfo>) list);
    }

    @GetMapping("audit/consultantsEditInfo")
    public Result<List<ConsultantsInfo>> consultantsEditInfo() {
        List<ConsultantsInfo> list = auditService.findEditConsultants();
        return Result.success(list);
    }

    @PostMapping("audit/successUpGrade")
    public Result<String> successUpGrade(@RequestBody ConsultantsInfo consultantsInfo) {
        System.out.println(consultantsInfo);
        auditService.successUpGrade(consultantsInfo);
        return Result.success(MessageConstant.AUDIT_SUCCESS);
    }

    @PostMapping("audit/failUpGrade")
    public Result<String> failUpGrade(@RequestBody ConsultantsInfo consultantsInfo) {
        auditService.failUpGrade(consultantsInfo);
        return Result.success(MessageConstant.AUDIT_FAIL);
    }

    @PostMapping("audit/successEdit")
    public Result<String> successEdit(@RequestBody ConsultantsInfo consultantsInfo) {
        auditService.successEdit(consultantsInfo);
        return Result.success(MessageConstant.AUDIT_SUCCESS);
    }

    @PostMapping("audit/failEdit")
    public Result<String> failEdit(@RequestBody ConsultantsInfo consultantsInfo) {
        auditService.failEdit(consultantsInfo);
        return Result.success(MessageConstant.AUDIT_SUCCESS);
    }

    @GetMapping("audit/upGrade")
    public Result<List<ConsultantsInfo>> ipGrade() {
        List<ConsultantsInfo> list = auditService.findUpGrade();
        return Result.success(list);
    }

    @GetMapping("audit/userInfo")
    public Result<List<UserInfo>> userInfoList() {
        List<UserInfo> list = auditService.findUserInfo();
        return Result.success(list);
    }

}
