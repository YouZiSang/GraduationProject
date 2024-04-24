package com.example.yzs.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Resource
    private JwtInterceptor jwtInterceptor;

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        //指定controller的统一接口的前缀,增加url前缀
        configurer.addPathPrefix("/api", clazz -> clazz.isAnnotationPresent(RestController.class));
    }

    //加入自定义拦截器JwtInterceptor,设置拦截规则

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor).addPathPatterns("/api/**")
                .excludePathPatterns("/api/user/login")
                .excludePathPatterns("/api/consultants/login")
                .excludePathPatterns("/api/user/register")
                .excludePathPatterns("/api/admin/login")
//                .excludePathPatterns("/yzs/admin/list")
//                .excludePathPatterns("/yzs/admin/userInfo")
        .excludePathPatterns("/api/captcha/img")
                .excludePathPatterns("/api/userInfo/register")
                .excludePathPatterns("/api/alipay/**")
                .excludePathPatterns("/api/chat")
                .excludePathPatterns("/api/upload/**")

//                .excludePathPatterns("/api/userInfo/getUserInfo")
        ;
    }
}
