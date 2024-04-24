package com.example.yzs.service.impl;

import com.example.yzs.constant.MessageConstant;
import com.example.yzs.dao.ConsultantsDao;
import com.example.yzs.entity.*;
import com.example.yzs.exception.CustomException;
import com.example.yzs.service.ConsultantsService;
import com.example.yzs.utils.Md5Util;
import com.example.yzs.utils.ThreadLocalUtil;
import com.example.yzs.utils.ValidatorUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ConsultantsServiceImpl implements ConsultantsService {

    @Autowired
    private ConsultantsDao consultantsDao;

    @Override
    public Account login(Account account) {
        // 1.使用ValidatorUtil类进行登录校验x
        ValidatorUtil.loginVerify(account);
        account = consultantsDao.login(account);


        if (account == null) {
            //如果查出来没有,那说明输入的用户名或者密码有误，提示用户，不允许登录
            throw new CustomException(MessageConstant.LOGIN_FAIL);
        }
        return account;
    }

    @Override
    public AppointmentSet addAppointment(AppointmentSet appointmentSet) {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer id = (Integer) map.get("cid");
        appointmentSet.setCid(id);

        consultantsDao.addAppointment(appointmentSet);
        return appointmentSet;
    }

    @Override
    public List<AppointmentSet> findApSet(Integer id) {
        return consultantsDao.findApSet(id);
    }


    @Override
    public PageBean<ConsultantsInfo> list(Integer currentPage,
                                          Integer pageSize,
                                          String selectName,
                                          Integer minAge,
                                          Integer maxAge) {
        //1,创建PageBean对象
        PageBean<ConsultantsInfo> pageBean = new PageBean<>();
        //2.开启分页查询PageHelp
        PageHelper.startPage(currentPage, pageSize);
        //3.调用mapper
        List<ConsultantsInfo> consultantsInfoList = consultantsDao.list(selectName, minAge, maxAge);
        //page中提供了方法，可以获取PageHelp分页查询后，得到的总记录条数和当前页数据
        Page<ConsultantsInfo> consultantsInfoPage = (Page<ConsultantsInfo>) consultantsInfoList;
        //把数据填充到PageBean对象中
        pageBean.setTotal(consultantsInfoPage.getTotal());
        pageBean.setItems(consultantsInfoPage.getResult());
        return pageBean;
    }


}
