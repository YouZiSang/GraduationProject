package com.example.yzs.controller.user;

import com.example.yzs.constant.CaptureConfig;
import com.example.yzs.constant.MessageConstant;
import com.example.yzs.constant.Result;
import com.example.yzs.entity.Account;
import com.example.yzs.entity.AppointmentSet;
import com.example.yzs.entity.Questionnaire;
import com.example.yzs.service.ConsultantsService;
import com.example.yzs.service.UserService;
import com.example.yzs.utils.JwtTokenUtils;
import com.example.yzs.utils.Md5Util;
import com.example.yzs.utils.RandomUtil;
import com.example.yzs.utils.ThreadLocalUtil;
import com.wf.captcha.utils.CaptchaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;


@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final ConsultantsService consultantsService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    public UserController(UserService userService, ConsultantsService consultantsService) {
        this.userService = userService;
        this.consultantsService = consultantsService;
    }


    /**
     * 处理用户登录请求的方法。
     *
     * @param user 登录用户的信息
     * @return 包含登录结果、消息和登录用户对象的Result对象
     */
    @PostMapping("/login")
    public Result<String> login(@RequestParam String key, @RequestBody Account account, HttpServletRequest request) {

        if (account.getVerCode().toLowerCase().equals(CaptureConfig.CAPTURE_MAP.get(key))) {
            userService.login(account);
            Map<String, Object> claims = new HashMap<>();
            claims.put("id", account.getId());
            claims.put("uid", account.getUid());
            String token = JwtTokenUtils.genToken(claims);
            HttpSession session = request.getSession();
            session.setAttribute("uid", account.getUid());
            CaptureConfig.CAPTURE_MAP.remove(key);
            ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
            operations.set(token, token, 10, TimeUnit.HOURS);
            return Result.success(MessageConstant.LOGIN_SUCCESS, token);
        } else {
            CaptchaUtil.clear(request);
            CaptureConfig.CAPTURE_MAP.remove(key);
            return Result.error(MessageConstant.CAPTCHA_ERROR);
        }

    }

    /**
     * 生成指定范围内的随机数字作为用户ID，并将用户信息添加到数据库中。
     *
     * @param user 要添加到数据库的用户对象
     * @return 包含操作结果和生成的用户ID的Result对象
     */
    @PostMapping("/register")
    public Result save(@RequestBody Account account) {
        System.out.println(account);
        String randomNum = RandomUtil.generateRandomNumber(7, 9);
        int uid = Integer.parseInt(randomNum);
        account.setUid(uid);
        System.out.println(account);
        userService.add(account);
        return Result.success(uid);
    }

    @PostMapping("updatePassword")
    public Result<String> updatePwd(@RequestBody Map<String, String> params, @RequestHeader("Authorization") String token) {
        //1.校验参数
        String oldPwd = params.get("odlPwd");
        String newPwd = params.get("newPwd");
        String rePwd = params.get("rePwd");
        //1.校验参数
        if (!StringUtils.hasLength(oldPwd) || !StringUtils.hasLength(newPwd) || !StringUtils.hasLength(rePwd)) {
            return Result.error(MessageConstant.FILLING_ERROR);
        }
        //原密码是否正确
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer id = (Integer) map.get("uid");
        Account loginAccount = userService.findById(id);

        if (!loginAccount.getPassword().equals(Md5Util.getMD5String(oldPwd))) {
            return Result.error(MessageConstant.ORIGINAL_PASSWORD_ERROR);
        }
        if (!rePwd.equals(newPwd)) {
            return Result.error(MessageConstant.PASSWORDS_ENTERED_DONOT_MATCH);
        }
        userService.updatePwd(id, newPwd);
        //删除redis中对应的token
        ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
        operations.getOperations().delete(token);
        return Result.success(MessageConstant.PASSWORDS_ENTERED_SUCCESS);
    }

    @GetMapping("/appointmentTable")
    public Result<List<AppointmentSet>> findConAppointment(@RequestParam("cid") Integer cid) {
        List<AppointmentSet> appointmentSet = consultantsService.findApSet(cid);
        return Result.success(appointmentSet);
    }

    @GetMapping("/name")
    public Result<Integer> getUsername(HttpSession session, HttpServletRequest request) {
//        Integer username = (Integer) request.getSession().getAttribute("userId");
        Integer username = (Integer) session.getAttribute("userId");
        System.out.println(username);
        return Result.success(username);
    }


}
