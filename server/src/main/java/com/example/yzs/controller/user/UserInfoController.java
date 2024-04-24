package com.example.yzs.controller.user;


import com.example.yzs.constant.MessageConstant;
import com.example.yzs.constant.Result;
import com.example.yzs.entity.ConsultantsInfo;
import com.example.yzs.entity.UserInfo;
import com.example.yzs.service.UserInfoService;
import com.example.yzs.utils.ThreadLocalUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/userInfo")
public class UserInfoController {

    private static final Logger log = LoggerFactory.getLogger(UserInfoController.class);
    @Resource
    private UserInfoService userInfoService;

    @GetMapping("/getUserAvatar")
    public Result findUserAvatar(@RequestHeader("Authorization") String token) {
        // 解析token，获取用户信息
//        User currentUser = JwtTokenUtils.getCurrentUser(token);
//
//        if (currentUser == null) {
//            return new Result("-1", "获取用户信息失败", null);
//        }
//
//        UserInfo userInfo = userInfoService.findAvatar(currentUser.getUser_id());
//        System.out.println(userInfo);
        return Result.success(MessageConstant.LOGIN_SUCCESS);
    }


    /**
     * 根据用户ID查询用户信息。
     *
     * @param id 用户ID
     * @return 包含查询结果、消息和用户信息对象的Result对象
     */
    @GetMapping("/getPersonalInfo")
    public Result findById() {
        //根据用户名查询用户
        Map<String,Object> map = ThreadLocalUtil.get();
        Integer id = (Integer) map.get("uid");
        UserInfo userInfo = userInfoService.findById(id);
        return Result.success(MessageConstant.LOGIN_SUCCESS,userInfo);
    }
    /**
     * 保存用户注册信息。
     *
     * @param userInfo 要保存的用户信息
     * @return 包含保存结果、消息和用户信息对象的Result对象
     */
    @PostMapping("/register")

    public Result save(@RequestBody UserInfo userInfo) {
        System.out.println(userInfo);
        userInfoService.add(userInfo);

        if (userInfo.getAvatarUrl() == null || userInfo.getAvatarUrl().isEmpty()) {
            userInfo.setAvatarUrl("resources/static/logo.png"); // 设置默认头像路径
        }
        return Result.success(MessageConstant.REGISTER_SUCCESS,userInfo);
    }

    @PostMapping("/editPersonalInfo")
    public Result edit(@RequestBody UserInfo userInfo){
        userInfoService.edit(userInfo);
        return Result.success(MessageConstant.EDIT_SUCCESS,userInfo);
    }

    @GetMapping("/list")
    public Result<List<UserInfo>> usersList(){
        List<UserInfo> usersList = userInfoService.getUsersList();
        return Result.success(usersList);
    }

    @PostMapping("upGrade")
    public Result<String> upGrade(@RequestBody ConsultantsInfo consultantsInfo){
        userInfoService.upGrade(consultantsInfo);
        return Result.success(MessageConstant.INFORMATION_FILL_SUCCESS);
    }

    @GetMapping("statusUpGrade")
    public Result<ConsultantsInfo> statusUpGrade(){
        Map<String,Object> map = ThreadLocalUtil.get();
        Integer id = (Integer) map.get("uid");
        ConsultantsInfo status = userInfoService.statusUpGrade(id);
        return Result.success(status);
    }
}
