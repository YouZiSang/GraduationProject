package com.example.yzs.controller.consultation;

import com.example.yzs.constant.MessageConstant;
import com.example.yzs.constant.Result;
import com.example.yzs.entity.consultation.ConsultationTable;
import com.example.yzs.entity.evaluation.ConsultationEvaluation;
import com.example.yzs.service.consultation.ConsultationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/consultation")
public class ConsultationController {

    private final ConsultationService consultationService;
    @Autowired
    public ConsultationController(ConsultationService consultationService) {
        this.consultationService = consultationService;
    }

    @PostMapping("add")
    public Result<String> add(@RequestBody ConsultationTable consultation) {
        consultationService.add(consultation);
        return Result.success("");
    }

    @GetMapping("table")
    public Result<List<ConsultationTable>> listTable(){
        List<ConsultationTable> list = consultationService.findTable();
        return Result.success(list);
    }

    @PostMapping("status")
    public Result<String> status(@RequestBody ConsultationTable consultationTable) {

        consultationService.updateStatus(consultationTable);
        return Result.success("");
    }

    @PostMapping("evaluation")
    public Result<String> evaluation(@RequestBody ConsultationEvaluation consultationEvaluation) {

        consultationService.addEvaluation(consultationEvaluation);
        return Result.success(MessageConstant.EVALUATION_SUCCESS);
    }

}
