package com.example.yzs.exception;

import com.example.yzs.constant.MessageConstant;
import com.example.yzs.constant.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice(basePackages = "com.example.yzs.controller")
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    //统一异常处理@ExceptionHandler,主要用于Exception
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result error(HttpServletRequest request, Exception e){
        log.error("异常信息:",e);
        return Result.error(MessageConstant.SYSTEM_ABNORMAL);
    }

    @ExceptionHandler(CustomException.class)
    @ResponseBody
    public Result error(HttpServletRequest request,CustomException e){

        return Result.error(e.getMsg());
    }
}
