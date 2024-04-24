package com.example.yzs.controller.api;

import cn.hutool.json.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.example.yzs.config.AliPayConfig;
import com.example.yzs.entity.shoppingcart.Orders;
import com.example.yzs.service.BookService;
import com.example.yzs.service.shoppingcart.OrdersService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/alipay")
public class AliPayController {
    private static final String GATEWAY_URL = "https://openapi-sandbox.dl.alipaydev.com/gateway.do";
    private static final String FORMAT = "JSON";
    private static final String CHARSET = "UTF-8";
    private static final String SIGN_TYPE = "RSA2";


    @Resource
    private AliPayConfig aliPayConfig;
    private final OrdersService ordersService;
    private final BookService bookService;

    public AliPayController(OrdersService ordersService, BookService bookService) {
        this.ordersService = ordersService;
        this.bookService = bookService;
    }

    @GetMapping("/pay")
    public void pay(String orderNo, HttpServletResponse httpResponse) throws Exception {
        //查询订单信息
        Orders orders = ordersService.selectByOrderNo(orderNo);
        if (orders == null )
        {
            return;
        }
        //1.创建Client，通用SDK提供的Client，负责调用支付宝的API
        AlipayClient alipayClient = new DefaultAlipayClient(GATEWAY_URL, aliPayConfig.getAppId(),
                aliPayConfig.getAppPrivateKey(), FORMAT, CHARSET, aliPayConfig.getAlipayPublicKey(), SIGN_TYPE);

        //2.创建Request并设置Request参数
        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();//发送请求的Request类
        request.setNotifyUrl(aliPayConfig.getNotifyUrl());
        JSONObject bizContent = new JSONObject();
        bizContent.set("out_trade_no", orders.getOrderNo());
        bizContent.set("total_amount", orders.getTotalPrice());
        bizContent.set("subject", orders.getGoodsName());
        bizContent.set("product_code", "FAST_INSTANT_TRADE_PAY");
        request.setBizContent(bizContent.toString());

        String form = "";
        try {
            form = alipayClient.pageExecute(request).getBody();//调用SDK生成表单
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        httpResponse.setContentType("text/html;charset=" + CHARSET);
        httpResponse.getWriter().write(form);//直接将完整的表单html输出到页面
        httpResponse.getWriter().flush();
        httpResponse.getWriter().close();
    }


    @PostMapping("/notify")
    public void payNotify(HttpServletRequest request) throws Exception {
        if(request.getParameter("trade_status").equals("TRADE_SUCCESS")){
            //回调
            Map<String,String> params = new HashMap<>();
            Map<String,String[]> requestParams = request.getParameterMap();
            for (String name : requestParams.keySet()){
                params.put(name,request.getParameter(name));
            }

            String sign = params.get("sign");
            String content = AlipaySignature.getSignCheckContentV1(params);
            boolean checkSignature = AlipaySignature.rsa256CheckContent(content,sign,aliPayConfig.getAlipayPublicKey(),"UTF-8");
            //支付宝验签
            if (checkSignature){
                String tradeNo = params.get("out_trade_no");
                String gmtPayment = params.get("gmt_payment");
                String alipayTradeNo = params.get("trade_no");

                System.out.println("交易名称:" + params.get( "subject"));
                System.out.println("交易状态:" + params.get( "trade_status"));
                System.out.println("支付宝交易凭证号:" + params.get( "trade_no"));
                System.out.println("商户订单号:" + params.get( "out_trade_no"));
                System.out.println("交易金额:" + params.get("total_amount"));
                System.out.println("买家在支付宝唯一id: " + params.get( "buyer_id"));
                System.out.println("买家付款时间:" + params.get("gmt_payment" ));
                System.out.println("买家付款金额:" + params.get( "buyer_pay_amount"));

                //更新订单状态为已支付，设置支付信息
                Orders orders = ordersService.selectByOrderNo(tradeNo);
                orders.setPayStatus("已支付");
                orders.setStatus("待发货");
                orders.setPayTime(Timestamp.valueOf(gmtPayment));
                orders.setPayNo(alipayTradeNo);
                ordersService.updateById(orders);
                bookService.updateBookSale(orders.getGoodsId(),orders.getNumber());

            }
        }


    }


}
