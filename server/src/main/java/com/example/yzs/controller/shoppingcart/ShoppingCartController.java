package com.example.yzs.controller.shoppingcart;

import com.example.yzs.constant.MessageConstant;
import com.example.yzs.constant.Result;
import com.example.yzs.entity.Books;
import com.example.yzs.entity.PageBean;
import com.example.yzs.entity.shoppingcart.ShoppingCart;
import com.example.yzs.exception.CustomException;
import com.example.yzs.service.shoppingcart.ShoppingCartService;
import com.example.yzs.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/shoppingCats")
public class ShoppingCartController {

    private final ShoppingCartService shoppingCartService;

    @Autowired
    public ShoppingCartController(ShoppingCartService shoppingCartService) {
        this.shoppingCartService = shoppingCartService;
    }

    @PostMapping("/addBooks")
    public Result<String> addProducts(@RequestBody ShoppingCart shoppingCart){

        System.out.println(shoppingCart);
        Map<Integer,Object> map = ThreadLocalUtil.get();
        Integer shopperId = (Integer) map.get("uid");
        if (shopperId == null)
        {
            shopperId = (Integer) map.get("cid");
        }

        Integer bookNumber = shoppingCart.getNumber();
        if (bookNumber > 0) {
            shoppingCartService.addProducts(shoppingCart,shopperId);
            return Result.success("卧槽");

        }
        return Result.error("添加失败");

    }

    @GetMapping("/selectBooks")
    public Result<List<ShoppingCart>> beanResult(@RequestParam("id")Integer shopperId){
        List<ShoppingCart> cartList = shoppingCartService.shoppingCartList(shopperId);

        return Result.success(cartList);
    }

    @PostMapping("/batchDeleteBooks")
    public Result<String> batchDelete(@RequestParam("id") Integer shopperId,@RequestBody List<ShoppingCart> selectedItems){
        shoppingCartService.batchDeleteBooks(shopperId,selectedItems);
        return Result.success(MessageConstant.BATCH_DELETE_SUCCESS);
    }

    @PostMapping("/deleteBooks")
    public Result<String> delete(@RequestParam("shopperId") Integer shopperId, @RequestParam("ISBN") String ISBN) {
        shoppingCartService.deleteBooks(shopperId,ISBN);
        return Result.success(MessageConstant.DELETE_SUCCESS);
    }

    @GetMapping("/selectBooksNumber")
    public Result<Map<String, Object>> result() {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer id = (Integer) map.get("uid");
        if (id == null) {
            id = (Integer) map.get("cid");
        }
        Map<String, Object> result = shoppingCartService.selectBooksNumber(id);
        return Result.success(result);
    }

    @PostMapping ("/updateNumber")
    public Result<String> updateNumber(@RequestBody ShoppingCart shoppingCart){
        Map<String,Object> map = ThreadLocalUtil.get();
        Integer id = (Integer) map.get("uid");
        if (id == null){
            id = (Integer) map.get("cid");
        }
        shoppingCartService.updateBooksNumber(shoppingCart,id);
        return Result.success("5");
    }



}
