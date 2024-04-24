package com.example.yzs.service.impl;

import com.example.yzs.dao.NoticeDao;
import com.example.yzs.entity.Notice;
import com.example.yzs.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class NoticeServiceImpl implements com.example.yzs.service.NoticeService{

    private final NoticeDao noticeDao;

    @Autowired
    public NoticeServiceImpl(NoticeDao noticeDao) {
        this.noticeDao = noticeDao;
    }

    @Override
    public void addNotice(Notice notice) {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer aid = (Integer) map.get("aid");
        notice.setAid(aid);
        noticeDao.addNotice(notice);
    }

    @Override
    public List<Notice> getAllNotice() {
        System.out.println("noticeDao.getAllNotice()");
        return noticeDao.getAllNotice();
    }

    @Override
    public void editNotice(Notice notice) {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer aid = (Integer) map.get("aid");
        notice.setAid(aid);
        noticeDao.editNotice(notice);
    }

    @Override
    public void deleteNotice(int id) {
        noticeDao.deleteNotice(id);
    }

    @Override
    public Notice getCarouselImg() {
        return noticeDao.getCarouselImg();
    }

    @Override
    public void editCarouselImg(Notice notice) {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer aid = (Integer) map.get("aid");
        notice.setAid(aid);
        noticeDao.editCarouselImg(notice);
    }

    @Override
    public int findEvaluationHigh() {
        return noticeDao.findEvaluationHigh();
    }
}
