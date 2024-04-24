package com.example.yzs.controller.shoppingcart;

import com.example.yzs.constant.MessageConstant;
import com.example.yzs.constant.Result;
import com.example.yzs.entity.PageBean;
import com.example.yzs.entity.evaluation.BookEvaluation;
import com.example.yzs.entity.shoppingcart.Orders;
import com.example.yzs.entity.shoppingcart.ShoppingCart;
import com.example.yzs.service.shoppingcart.OrdersService;
import com.example.yzs.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/orders")
public class OrdersController {
    private final OrdersService ordersService;

    @Autowired
    public OrdersController(OrdersService ordersService) {
        this.ordersService = ordersService;
    }

    @PostMapping("/buyBooks")
    public Result<String> buyBooks(@RequestParam("id") Integer shopperId, @RequestBody List<ShoppingCart> selectedItems){
        System.out.println(selectedItems);
        ordersService.buyBooks(shopperId,selectedItems);
        return Result.success(MessageConstant.PLACE_AN_ORDER);
    }

    @GetMapping("/selectOrders")
    public Result<PageBean<Orders>> listResult(Integer currentPage,
                                           Integer pageSize,
                                           @RequestParam(required = false) String selectCondition){

        Map<String,Object> map = ThreadLocalUtil.get();
        Integer buyerId = (Integer) map.get("uid");
        if (buyerId == null) {
            buyerId = (Integer) map.get("cid");
        }
       PageBean<Orders> ordersPageBean = ordersService.selectOrders(buyerId,currentPage,pageSize,selectCondition);
        return Result.success(ordersPageBean);
    }

    @PostMapping("updateStatus")
    public Result<String> updateStatus(@RequestBody Orders orders){
        ordersService.updateStatus(orders);
        return Result.success(MessageConstant.RECEIPT_SUCCESS);
    }

    @PostMapping("evaluation")
    public Result<String> evaluation(@RequestBody BookEvaluation evaluation) {
        System.out.println(evaluation);
        Map<String,Object> map = ThreadLocalUtil.get();
        Integer evaId = (Integer) map.get("uid");
        if (evaId == null) {
            evaId = (Integer) map.get("cid");
        }
        ordersService.evaluation(evaluation,evaId);
        return Result.success(MessageConstant.EVALUATION_SUCCESS);
    }

    @PostMapping("delOrder")
    public Result<String> delOrder(@RequestParam("orderNo") String orderNo) {
        System.out.println(orderNo);
        ordersService.delOrder(orderNo);
        return Result.success(MessageConstant.DELETE_ORDER_SUCCESS);
    }

}
