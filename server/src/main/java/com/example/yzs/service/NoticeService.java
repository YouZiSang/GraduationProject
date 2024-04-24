package com.example.yzs.service;

import com.example.yzs.entity.Notice;

import java.util.List;

public interface NoticeService {
    void addNotice(Notice notice);

    List<Notice> getAllNotice();

    void editNotice(Notice notice);

    void deleteNotice(int id);

    Notice getCarouselImg();

    void editCarouselImg(Notice notice);


    int findEvaluationHigh();
}
