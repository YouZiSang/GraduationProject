package com.example.yzs.controller.consultants;

import com.example.yzs.constant.MessageConstant;
import com.example.yzs.constant.Result;
import com.example.yzs.entity.ConsultantsInfo;
import com.example.yzs.entity.evaluation.ConsultationEvaluation;
import com.example.yzs.service.ConsultantsInfoService;
import com.example.yzs.utils.ThreadLocalUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/consultants")
public class ConsultantsInfoController {



    private final ConsultantsInfoService consultantsInfoService;
    @Autowired
    public ConsultantsInfoController(ConsultantsInfoService consultantsInfoService) {
        this.consultantsInfoService = consultantsInfoService;
    }

    @PostMapping("/list")
    public Result<List<ConsultantsInfo>> listResult(){

        return Result.success(consultantsInfoService.consultantsInfoList());
    }

    @GetMapping("info")
    public Result<ConsultantsInfo> consultantsInfoResult(){
        Map<String,Object> map = ThreadLocalUtil.get();
        Integer id = (Integer) map.get("cid");
        return Result.success(consultantsInfoService.personalInfo(id));
    }

    @PostMapping("updateInfo")
    public Result<String> updateInfo(@RequestBody ConsultantsInfo consultantsInfo) {
        consultantsInfoService.updateInfo(consultantsInfo);
        return Result.success(MessageConstant.INFORMATION_FILL_SUCCESS);
    }

    @GetMapping("statusEdit")
    public Result<ConsultantsInfo> statusEdit(){
        Map<String,Object> map = ThreadLocalUtil.get();
        Integer id = (Integer) map.get("cid");
        ConsultantsInfo status = consultantsInfoService.getStatusEdit(id);
        return Result.success(status);
    }

    @GetMapping("evaluation")
    public Result<List<ConsultationEvaluation>> evaluation(Integer cid){
        return Result.success(consultantsInfoService.evaluation(cid));
    }
}
