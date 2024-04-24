package com.example.yzs.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.example.yzs.constant.CaptureConfig;
import com.example.yzs.constant.MessageConstant;
import com.example.yzs.constant.Result;
import com.example.yzs.entity.*;
import com.example.yzs.entity.shoppingcart.Orders;
import com.example.yzs.service.AdminService;
import com.example.yzs.utils.JwtTokenUtils;
import com.example.yzs.utils.ThreadLocalUtil;
import com.wf.captcha.utils.CaptchaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;


@RestController
@RequestMapping("/admin")
public class AdminController {

    private final StringRedisTemplate stringRedisTemplate;
    private final AdminService adminService;

    @Autowired
    public AdminController(StringRedisTemplate stringRedisTemplate, AdminService adminService) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.adminService = adminService;
    }


    /**
     * 获取所有管理员列表。
     *
     * @return 包含查询结果和管理员列表的Result对象
     */
    @GetMapping("/list")
    public Result<List<Account>> findAll() {
        List<Account> adminList = adminService.finAll();
        return Result.success(adminList);
    }

    /**
     * 管理员登录验证。
     *
     * @param account 管理员信息
     * @param key     验证码的键
     * @param request HTTP请求对象
     * @return 包含登录结果和消息的Result对象
     */
    @PostMapping("/login")
    public Result login(@RequestBody Account account, @RequestParam String key, HttpServletRequest request) {

        if (account.getVerCode().toLowerCase().equals(CaptureConfig.CAPTURE_MAP.get(key))) {
            adminService.login(account);
            Map<String, Object> claims = new HashMap<>();
            claims.put("id", account.getId());
            claims.put("aid", account.getUid());
            String token = JwtTokenUtils.genToken(claims);
            HttpSession session = request.getSession();
            session.setAttribute("aid", account.getUid());
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
     * 获取所有用户信息。
     *
     * @return 包含查询结果和用户列表的Result对象
     */
    @GetMapping("/userInfo")
    public Result AllUserInfo() {
        List<UserInfo> userList = adminService.findAllUser();
        return Result.success(userList);
    }

    @GetMapping("/usersList")
    public Result<PageBean<UserInfo>> usersList(
            Integer currentPage,
            Integer pageSize,
            @RequestParam(required = false) String selectCondition,
            @RequestParam(required = false) Integer minAge,
            @RequestParam(required = false) Integer maxAge) {
        PageBean<UserInfo> pageBean = adminService.usersList(currentPage, pageSize, selectCondition, minAge, maxAge);
        return Result.success(pageBean);
    }

    @PostMapping("deleteUser/{uid}/{deleted}")
    public Result<String> deleteUser(@PathVariable Integer uid, @PathVariable Integer deleted) {
        adminService.deleteUser(uid, deleted);
        return Result.success(MessageConstant.DELETE_SUCCESS);

    }

    @PostMapping("deleteConsultant/{cid}/{deleted}")
    public Result<String> deleteConsultant(@PathVariable Integer cid, @PathVariable Integer deleted) {
        adminService.deleteConsultant(cid, deleted);
        return Result.success(MessageConstant.DELETE_SUCCESS);

    }

    @GetMapping("/admin/adminPersonalInfo")
    public Result result() {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer id = (Integer) map.get("uid");
        Account account = adminService.findId(id);
        return Result.success(account);
    }

    @GetMapping("/getAllBook")
    public Result<List<Books>> allBook() {
        List<Books> booksList = adminService.findAllBook();

        return Result.success(booksList);
    }

    @PostMapping("addBookInfo")
    public Result<String> addBook(@RequestBody Books books) {
        adminService.addBooks(books);
        return Result.success(MessageConstant.BOOK_INFO_ADD_SUCCESS);
    }

    @PostMapping("updateBookInfo")
    public Result<String> updateBookInfo(@RequestBody Books books) {
        adminService.updateBooksInfo(books);
        return Result.success(MessageConstant.BOOK_INFO_EDIT_SUCCESS);
    }

    @PostMapping("delBookInfo")
    public Result<String> delBookInfo(@RequestBody Books books) {
        adminService.delBooksInfo(books);
        return Result.success(MessageConstant.BOOK_INFO_DELETE_SUCCESS);
    }

    @PostMapping("searchBookInfo")
    public Result<List<Books>> searchBookInfo(@RequestParam("search") String search) {
        List<Books> booksList = adminService.searchBookInfo(search);
        return Result.success(booksList);
    }

    @PostMapping("searchBookTypeInfo")
    public Result<List<Books>> searchBookTypeInfo(@RequestParam("type") String search) {
        List<Books> booksList = adminService.searchBookTypeInfo(search);
        return Result.success(booksList);
    }

    @GetMapping("export")
    public void export(@RequestParam(required = false) String searchBook,
                       @RequestParam(required = false) String ids, HttpServletResponse response) throws IOException {
        ExcelWriter writer = ExcelUtil.getWriter(true);
        List<Books> list = new ArrayList<>();
        if (!StrUtil.isNotBlank(searchBook) && ids.isEmpty()) {
            list = adminService.findAllBook();
        } else if (StrUtil.isNotBlank(searchBook)) {
            list = adminService.searchBookInfo(searchBook);
        } else if (!ids.isEmpty()) {
            String[] split = ids.split(",");
            for (String id : split) {
                Books books = adminService.findBookById(Integer.parseInt(id));
                list.add(books);
            }
        }
        writer.write(list, true);
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        response.setHeader("Content-Disposition", "attachment;filename" + URLEncoder.encode("图书信息表.xls", "utf-8"));
        ServletOutputStream outputStream = response.getOutputStream();
        writer.flush(outputStream, true);
        outputStream.close();
    }

    @PostMapping("importBooks")
    public Result<String> importBooks(MultipartFile file) throws IOException {
        ExcelReader reader = ExcelUtil.getReader(file.getInputStream());
        List<Books> books = reader.readAll(Books.class);
        adminService.saveBatch(books);
        return Result.success(MessageConstant.IMPORT_SUCCESS);
    }

    @GetMapping("findAllOrders")
    public Result<PageBean<Orders>> ordersResult(Integer currentPage,
                                                 Integer pageSize,
                                                 @RequestParam(required = false) String selectName,
                                                 @RequestParam(required = false) String selectGoodsName) {

        PageBean<Orders> allOrders = adminService.findAllOrders(currentPage, pageSize, selectName, selectGoodsName);
        return Result.success(allOrders);
    }

    @PostMapping("updateOrderStatus")
    public Result<String> updateStatus(@RequestBody Orders orders) {
        adminService.updateStatus(orders);
        return Result.success(MessageConstant.DELIVERY_SUCCESS);
    }

    @GetMapping("findType")
    public Result<List<Type>> findType() {
        return Result.success(adminService.findType());
    }
}
