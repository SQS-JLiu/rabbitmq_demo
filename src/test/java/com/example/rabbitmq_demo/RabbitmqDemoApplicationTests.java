package com.example.rabbitmq_demo;

import com.example.rabbitmq_demo.controller.OrderController;
import com.example.rabbitmq_demo.resource.domain.gen.OrderDO;
import com.example.rabbitmq_demo.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

@SpringBootTest
class RabbitmqDemoApplicationTests {

//	@Autowired
//	private OrderController orderController;

	@Autowired
	private OrderService orderService;

	@Test
	public void testSend(){
		//发送一个消息到消息队列
		//orderController.addOrder();
	}

	@Test
	public void testRabbitSend(){
		OrderDO orderDO = new OrderDO();
		orderDO.setId("20200911002");
		orderDO.setName("测试创建订单");
		orderDO.setMessageId(System.currentTimeMillis()+"$"+ UUID.randomUUID().toString());//UUID含义是通用唯一识别码
		orderService.createOrder(orderDO);
	}

}
