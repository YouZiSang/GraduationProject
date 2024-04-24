package com.example.yzs.component;

import com.example.yzs.dao.shoppingcart.OrdersDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

@Component
public class OrderCancellationTask {


    private final OrdersDao ordersDao;


    private final StringRedisTemplate stringRedisTemplate;
    @Autowired
    public OrderCancellationTask(OrdersDao ordersDao, StringRedisTemplate stringRedisTemplate) {
        this.ordersDao = ordersDao;
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Scheduled(fixedRate = 60000) // 每分钟执行一次
    public void cancelExpiredOrders()  {
        Set<String> orderKeys = stringRedisTemplate.keys("order:*");

        for (String orderKey : orderKeys) {
            String orderExpireTime = stringRedisTemplate.opsForValue().get(orderKey);

            long expireTime = Long.parseLong(orderExpireTime); // 将时间戳转换为长整型

            if (expireTime < System.currentTimeMillis()) {
                // 订单已过期，执行取消订单操作
                String orderNo = orderKey.split(":")[1];
                ordersDao.delOrder(orderNo);
                stringRedisTemplate.delete(orderKey);
                System.out.println("订单已自动取消：" + orderKey);
            }
        }
    }
}