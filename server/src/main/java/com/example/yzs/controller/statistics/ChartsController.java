package com.example.yzs.controller.statistics;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.example.yzs.constant.Result;
import com.example.yzs.entity.Books;
import com.example.yzs.entity.Questionnaire;
import com.example.yzs.service.AdminService;
import com.example.yzs.service.QuestionnaireService;
import com.example.yzs.service.consultation.AppointmentService;
import com.example.yzs.service.shoppingcart.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/charts")
public class ChartsController {

    private final AdminService adminService;
    private final QuestionnaireService questionnaireService;
    private final OrdersService ordersService;
    private final AppointmentService appointmentService;

    @Autowired
    public ChartsController(AdminService adminService, QuestionnaireService questionnaireService, OrdersService ordersService, AppointmentService appointmentService) {
        this.adminService = adminService;
        this.questionnaireService = questionnaireService;
        this.ordersService = ordersService;
        this.appointmentService = appointmentService;
    }


    @GetMapping("books")
    public Result<List<Map<String, Object>>> bie() {
        List<Books> booksList = adminService.findAllBook();
        Map<String, Long> collect = booksList.stream().filter(x -> ObjectUtil
                        .isNotEmpty(x.getBookType()))
                .collect(Collectors.groupingBy(Books::getBookType, Collectors.summingLong(Books::getNumber)));
        List<Map<String, Object>> mapList = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(collect)) {
            for (String key : collect.keySet()) {
                Map<String, Object> map = new java.util.HashMap<>();
                map.put("value", collect.get(key));
                map.put("name", key);
                mapList.add(map);
            }
        }
        return Result.success(mapList);
    }

    @GetMapping("scores")
    public Result<List<Map<String, Object>>> typeScores() {
        List<Questionnaire> questionnaireList = questionnaireService.echartsTypeScores();
        Map<String, Long> typeScoreSum = questionnaireList.stream()
                .filter(x -> ObjectUtil.isNotEmpty(x.getTypeName()))
                .collect(Collectors.groupingBy(Questionnaire::getTypeName, Collectors.summingLong(Questionnaire::getTypeScores)));

        Map<String, Long> typeCount = questionnaireList.stream()
                .filter(x -> ObjectUtil.isNotEmpty(x.getTypeName()))
                .collect(Collectors.groupingBy(Questionnaire::getTypeName, Collectors.counting()));

        List<Map<String, Object>> mapList = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(typeCount)) {
            for (String key : typeCount.keySet()) {
                Map<String, Object> map = new HashMap<>();
                map.put("name", key);
                map.put("count", typeCount.get(key)); // 人数
                map.put("value", typeScoreSum.get(key)); // 总分
                mapList.add(map);
            }
        }

        return Result.success(mapList);
    }

    @GetMapping("sales")
    public Result<List<Map<String, Object>>> sales(){
        List<String> dates = ordersService.salesEcharts();

        // 使用 Java Stream API 对数据进行处理
        Map<String, Integer> result = dates.stream()
                // 过滤掉 null 值
                .filter(date -> date != null)
                // 将日期字符串转换为年月作为键，将相同年月的数据加起来
                .map(date -> date.substring(0, 7)) // 仅保留年月部分
                .collect(Collectors.groupingBy(
                        date -> date, // 提取年月部分作为键
                        TreeMap::new, // 使用 TreeMap 保持顺序
                        Collectors.summingInt(date -> 1) // 统计相同年月的数量
                ));

        List<Map<String, Object>> mapList = new ArrayList<>();

        // 将结果添加到 mapList 中
        result.forEach((yearMonth, count) -> {
            Map<String, Object> map = new HashMap<>();
            map.put("month", yearMonth);
            map.put("sales", count);
            mapList.add(map);
        });
        return Result.success(mapList);
    }

    @GetMapping("appointmentNumByCid")
    public Result<List<Map<String, Object>>> appointmentNumByCid() {
        List<String> dates = appointmentService.appointmentNumByCid();

        // 使用LinkedHashMap来保持插入顺序
        Map<String, Long> dateSumMap = new LinkedHashMap<>();
        dates.forEach(date -> dateSumMap.put(date.substring(5), dateSumMap.getOrDefault(date.substring(5), 0L) + 1));

        List<Map<String, Object>> mapList = new ArrayList<>();
        // 输出结果
        dateSumMap.forEach((date, number) -> {
            Map<String, Object> map = new HashMap<>();
            map.put("date", date); // 日期
            map.put("number", number); // 销售数量
            mapList.add(map);
        });

        return Result.success(mapList);
    }

    @GetMapping("appointmentNumber")
    public Result<List<Map<String, Object>>> appointmentNumber() {
        List<String> dates = appointmentService.appointmentNumber();
        // 使用LinkedHashMap来保持插入顺序
        Map<String, Long> mapDates = new LinkedHashMap<>();
        dates.forEach(date -> mapDates.put(date.substring(5), mapDates.getOrDefault(date.substring(5), 0L) + 1));

        List<Map<String, Object>> mapList = new ArrayList<>();
        // 输出结果
        mapDates.forEach((date, number) -> {
            Map<String, Object> map = new HashMap<>();
            map.put("date", date); // 日期
            map.put("number", number); // 销售数量
            mapList.add(map);
        });

        return Result.success(mapList);
    }
}
