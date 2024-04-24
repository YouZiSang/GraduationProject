package com.example.yzs.controller;

import com.example.yzs.constant.CaptureConfig;
import com.wf.captcha.SpecCaptcha;
import com.wf.captcha.base.Captcha;
import com.wf.captcha.utils.CaptchaUtil;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/captcha")
public class CaptureController {

    @RequestMapping("/img")
    public void captcha(@RequestParam String key, HttpServletRequest request, HttpServletResponse response) throws Exception{
        //png类型
        SpecCaptcha captcha = new SpecCaptcha(120,45,4);//图片宽高及字符个数
        captcha.setCharType(Captcha.TYPE_NUM_AND_UPPER);
        //把验证码在后台保存一份，但是不能保存在session，可以保存在redis，也可以保存在后台的某个Map里
        CaptureConfig.CAPTURE_MAP.put(key,captcha.text().toLowerCase());
        CaptchaUtil.out(captcha,request,response);


        //算术类型
//        ArithmeticCaptcha captcha = new ArithmeticCaptcha(120,45);
//        captcha.setLen(3);    //几位数运算
//        captcha.getArithmeticString();    //获取运算的公式
//        captcha.text();   //获取结果
//        CaptureConfig.CAPTURE_MAP.put(key,captcha.text().toLowerCase());
//        CaptchaUtil.out(captcha,request,response);
    }
}
