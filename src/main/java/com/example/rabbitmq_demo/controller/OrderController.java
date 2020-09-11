package com.example.rabbitmq_demo.controller;

import com.example.rabbitmq_demo.entity.Order;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;

//@RestController
public class OrderController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    //order-exchange可要在RabbitMQ中创建，比如在控制台页面的Exchange中创建
    //order-queue消息队列也可在RabbitMQ中创建，比如在控制台页面的Queue中创建
    //并在页面中为order-exchange绑定order-queue，添加RoutingKey为order.abcd
    //order.* 可匹配order.abcd, order.aaaa, order.bbbb
    //order.# 可匹配order.abcd, order.abcd.aaaa
    public void addOrder(){
        Order order = new Order();
        CorrelationData correlationData = new CorrelationData();
        correlationData.setId(order.getMessageId());
        rabbitTemplate.convertAndSend("order-exchange", //exchange
                "order.abcd",  // routingKey
                order, //消息体内容
                correlationData); //correlationData消息唯一ID
    }

    @RabbitHandler  //注解形式消费, 自动监听并消费
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "order-queue",durable = "true"),
            exchange = @Exchange(value = "order-exchange",durable = "true",type = "topic"),
            key = "order2.abc"
    ))  //配置监听数据, RabbitMQ中若没有该Queue或Exchange,则会自动创建并绑定
    public void getOrder(@Payload Order order,
                         @Headers Map<String,Object> headers,
                         Channel channel) throws IOException {
        //消费者操作
        System.out.println("--------收到消息，开始消费------");
        System.out.println("订单ID: "+order.getId());

        // ACK
        Long delivery_tag =(Long) headers.get(AmqpHeaders.DELIVERY_TAG);
        channel.basicAck(delivery_tag,false); //手工签收数据, 推荐使用手工签收
    }
}
