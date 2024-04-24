package com.example.yzs.controller;

import com.example.yzs.constant.Result;
import com.example.yzs.entity.Notice;
import com.example.yzs.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("notice")
@RestController
public class NoticeController {
    private final NoticeService noticeService;
    @Autowired
    public NoticeController(NoticeService noticeService) {
        this.noticeService = noticeService;
    }

    @PostMapping("add")
    public Result<String> addNotice(@RequestBody Notice notice) {
        noticeService.addNotice(notice);
        return Result.success("add notice success");
    }

    @GetMapping("getAll")
    public Result<List<Notice>> getAllNotice() {
        return Result.success(noticeService.getAllNotice());
    }

    @PostMapping("edit")
    public Result<String> editNotice(@RequestBody Notice notice) {
        noticeService.editNotice(notice);
        return Result.success("edit notice success");
    }

    @PostMapping ("/edit/{id}")
    public Result<String> deleteNotice(@PathVariable("id") int id) {
        noticeService.deleteNotice(id);
        return Result.success("delete notice success");
    }

    @GetMapping("carouselImg")
    public Result<Notice> getCarouselImg() {
        return Result.success(noticeService.getCarouselImg());
    }

    @PostMapping("editCarousel")
    public Result<String> editCarouselImg(@RequestBody Notice notice) {
        noticeService.editCarouselImg(notice);
        return Result.success("edit carousel img success");
    }

    @GetMapping("settle")
    public Result<Integer> settle() {
        int highEvaluationCount = noticeService.findEvaluationHigh();
        return Result.success(highEvaluationCount);
    }


}
