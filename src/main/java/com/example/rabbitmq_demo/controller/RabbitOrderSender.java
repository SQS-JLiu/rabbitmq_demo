package com.example.rabbitmq_demo.controller;

import com.example.rabbitmq_demo.constant.Constants;
import com.example.rabbitmq_demo.resource.domain.gen.OrderDO;
import com.example.rabbitmq_demo.resource.mapping.gen.BrokerMsgLogDOMapper;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class RabbitOrderSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private BrokerMsgLogDOMapper brokerMsgLogDOMapper;

    //回调函数：confirm确认
    final RabbitTemplate.ConfirmCallback confirmCallback = new RabbitTemplate.ConfirmCallback(){

        @Override
        public void confirm(CorrelationData correlationData, boolean ack, String s) {
            System.out.println("correlationData: "+confirmCallback);
            String messageId = correlationData.getId();
            if(ack){
                //如果confirm返回成功 则进行更新
                System.out.println("-------confirm返回成功 进行更新-------------------");
                brokerMsgLogDOMapper.changeBrokerMessageLogStatus(messageId, Constants.ORDER_SEND_SUCCESS,new Date());
            }else {
                //失败处理，可能消息队列已满等问题
                System.err.println("---异常处理...---");
            }
        }
    };

    //发送消息方法调用：构建自定义对象消息
    public void sendOrder(OrderDO orderDO){
        rabbitTemplate.setConfirmCallback(confirmCallback);
        //消息唯一ID
        CorrelationData correlationData = new CorrelationData(orderDO.getMessageId());
        // 可以将exchange修改为一个不存在的exchange来模式投递消息的场景 例如：order-exchange11111
        rabbitTemplate.convertAndSend("order-exchange","order.ABC",orderDO,correlationData);
    }
}
