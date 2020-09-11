package com.example.rabbitmq_demo.controller;

import com.example.rabbitmq_demo.resource.domain.gen.OrderDO;
import com.example.rabbitmq_demo.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/create")
    public String testCreateOrder(){
        OrderDO orderDO = new OrderDO();
        orderDO.setId("20200911001");
        orderDO.setName("测试创建订单");
        orderDO.setMessageId(System.currentTimeMillis()+"$"+ UUID.randomUUID().toString());//UUID含义是通用唯一识别码
        orderService.createOrder(orderDO);
        return "success";
    }
}
