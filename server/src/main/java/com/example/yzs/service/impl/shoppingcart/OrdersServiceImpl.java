package com.example.yzs.service.impl.shoppingcart;

import com.example.yzs.dao.shoppingcart.OrdersDao;
import com.example.yzs.dao.shoppingcart.ShoppingCartDao;
import com.example.yzs.entity.PageBean;
import com.example.yzs.entity.evaluation.BookEvaluation;
import com.example.yzs.entity.shoppingcart.Orders;
import com.example.yzs.entity.shoppingcart.ShoppingCart;
import com.example.yzs.service.shoppingcart.OrdersService;
import com.example.yzs.utils.UUIDUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class OrdersServiceImpl implements OrdersService {
    private final OrdersDao ordersDao;
    private final ShoppingCartDao shoppingCartDao;

    private final StringRedisTemplate stringRedisTemplate;

    public OrdersServiceImpl(OrdersDao ordersDao, ShoppingCartDao shoppingCartDao, StringRedisTemplate stringRedisTemplate) {
        this.ordersDao = ordersDao;
        this.shoppingCartDao = shoppingCartDao;
        this.stringRedisTemplate = stringRedisTemplate;
    }


    @Override
    public void buyBooks(Integer shopperId, List<ShoppingCart> selectedItems) {

        for (ShoppingCart item : selectedItems) {
            Orders orders = new Orders();
            orders.setOrderNo(UUIDUtils.orderNo());
            orders.setGoodsId(item.getGoodsId());
            orders.setGoodsName(item.getTitle());
            orders.setNumber(item.getNumber());
            orders.setTotalPrice(item.getTotalPrice());
            orders.setPayStatus("未支付");
            orders.setBuyerId(shopperId);
            orders.setDeliveryAdd(item.getDeliveryAdd());

            // 向Redis添加订单过期键
            ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
            long expireAt = System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(30); // 当前时间+30分钟
            operations.set("order:" + orders.getOrderNo(), String.valueOf(expireAt));
            Date expireDate = new Date(expireAt);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String formattedExpireTime = sdf.format(expireDate);
            ordersDao.addOrders(orders,formattedExpireTime);
            shoppingCartDao.batchDelete(shopperId, selectedItems);
//            String orderKey = "order:" + orders.getOrderNo(); // 可以根据订单号构建键
//            String orderData = serializeOrder(orders); // 将订单对象序列化为字符串
//                   ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
//        operations.set(orders,orders,12, TimeUnit.HOURS);
        }

    }

    @Override
    public PageBean<Orders> selectOrders(Integer buyerId, Integer currentPage, Integer pageSize, String selectCondition) {
        //1,创建PageBean对象
        PageBean<Orders> pageBean = new PageBean<>();
        //2.开启分页查询PageHelp
        PageHelper.startPage(currentPage, pageSize);
        //3.调用mapper
        List<Orders> ordersList = ordersDao.selectOrders(buyerId, selectCondition);
        //page中提供了方法，可以获取PageHelp分页查询后，得到的总记录条数和当前页数据
        Page<Orders> ordersPage = (Page<Orders>) ordersList;
        //把数据填充到PageBean对象中
        pageBean.setTotal(ordersPage.getTotal());
        pageBean.setItems(ordersPage.getResult());
        return pageBean;
    }

    @Override
    public void updateStatus(Orders orders) {
        ordersDao.updateStatus(orders);

    }


    @Override
    public Orders selectByOrderNo(String orderNo) {
        return ordersDao.selectByOrdersNo(orderNo);
    }

    @Override
    public void updateById(Orders orders) {
        ordersDao.updateById(orders);
    }

    @Override
    public void evaluation(BookEvaluation evaluation, Integer evaId) {

        if (evaluation.getMedia() != null){
            System.out.println(evaluation.getMedia());
            String mediaString = String.join(",", evaluation.getMedia())
                    .substring(2, evaluation.getMedia().length() - 2);
            ordersDao.evaluationOrder(evaluation, mediaString, evaId);
        }else {
            String mediaString =null;
            ordersDao.evaluationOrder(evaluation, mediaString, evaId);
        }
    }

    @Override
    public void delOrder(String orderNo) {
        ordersDao.delOrder(orderNo);
    }

    @Override
    public List<String> salesEcharts() {
        return ordersDao.salesEcharts();
    }


}
