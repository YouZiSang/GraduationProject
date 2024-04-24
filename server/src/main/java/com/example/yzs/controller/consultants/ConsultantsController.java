package com.example.yzs.controller.consultants;

import com.example.yzs.constant.CaptureConfig;
import com.example.yzs.constant.MessageConstant;
import com.example.yzs.constant.Result;
import com.example.yzs.entity.*;
import com.example.yzs.service.ConsultantsService;
import com.example.yzs.utils.JwtTokenUtils;
import com.example.yzs.utils.ThreadLocalUtil;
import com.wf.captcha.utils.CaptchaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/consultants")
public class ConsultantsController {

    @Autowired
    private ConsultantsService consultantsService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @PostMapping("/login")
    public Result login(@RequestParam String key, @RequestBody Account account, HttpServletRequest request) {



        if (account.getVerCode().toLowerCase().equals(CaptureConfig.CAPTURE_MAP.get(key))) {
            consultantsService.login(account);
            Map<String, Object> claims = new HashMap<>();
            claims.put("id", account.getId());
            claims.put("cid", account.getUid());
            String token = JwtTokenUtils.genToken(claims);
            HttpSession session = request.getSession();
            session.setAttribute("cid", account.getUid());
            //把token传到Redis中
            ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
            operations.set(token, token, 12, TimeUnit.HOURS);

            return Result.success(MessageConstant.LOGIN_SUCCESS, token);
        } else {
            CaptchaUtil.clear(request);
            CaptureConfig.CAPTURE_MAP.remove(key);
            return Result.error(MessageConstant.CAPTCHA_ERROR);
        }

    }


    @GetMapping("/list")
    public Result<PageBean<ConsultantsInfo>> list(
            Integer currentPage,
            Integer pageSize,
            @RequestParam(required = false) String selectName,
            @RequestParam(required = false) Integer minAge,
            @RequestParam(required = false) Integer maxAge) {
        PageBean<ConsultantsInfo> pageBean = consultantsService.list(currentPage, pageSize, selectName, minAge, maxAge);
        return Result.success(pageBean);
    }

    @PostMapping("/manage")
    public Result addAppointment(@RequestBody AppointmentSet appointmentSet) {
        appointmentSet = consultantsService.addAppointment(appointmentSet);
        return Result.success("成了", appointmentSet);
    }

    @GetMapping("/findAppointment")
    public Result<List<AppointmentSet>> findApSet() {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer id = (Integer) map.get("cid");
        List<AppointmentSet> appointmentSet = consultantsService.findApSet(id);
        return Result.success(appointmentSet);
    }

}
