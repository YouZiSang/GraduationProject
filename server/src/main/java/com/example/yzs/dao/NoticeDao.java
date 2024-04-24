package com.example.yzs.dao;

import com.example.yzs.entity.Notice;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface NoticeDao {
    void addNotice(Notice notice);

    List<Notice> getAllNotice();

    void editNotice(Notice notice);

    void deleteNotice(int id);

    Notice getCarouselImg();

    void editCarouselImg(Notice notice);

    int findEvaluationHigh();
}
