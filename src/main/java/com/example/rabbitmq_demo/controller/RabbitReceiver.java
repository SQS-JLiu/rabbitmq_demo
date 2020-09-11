package com.example.rabbitmq_demo.controller;

import com.example.rabbitmq_demo.resource.domain.gen.OrderDO;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
public class RabbitReceiver {

    @RabbitHandler  //注解形式消费, 自动监听并消费
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "order-queue",durable = "true"),
            exchange = @Exchange(value = "order-exchange",durable = "true",type = "topic"),
            key = "order.#"
    ))  //配置监听数据, RabbitMQ中若没有该Queue或Exchange,则会自动创建并绑定
    public void onOrderMessage(@Payload OrderDO order,
                         @Headers Map<String,Object> headers,
                         Channel channel) throws IOException {
        //消费者操作
        System.out.println("--------收到消息，开始消费------");
        System.out.println("消费端order: "+order.getId());
        // ACK
        Long delivery_tag =(Long) headers.get(AmqpHeaders.DELIVERY_TAG);
        channel.basicAck(delivery_tag,false); //手工签收数据, 推荐使用手工签收, 不批量确认
        System.out.println("----------------------------------------------------");
    }
}
