package com.example.rabbitmq_demo.service;

import com.alibaba.fastjson.JSON;
import com.example.rabbitmq_demo.constant.Constants;
import com.example.rabbitmq_demo.controller.RabbitOrderSender;
import com.example.rabbitmq_demo.resource.domain.gen.BrokerMsgLogDO;
import com.example.rabbitmq_demo.resource.domain.gen.OrderDO;
import com.example.rabbitmq_demo.resource.mapping.gen.BrokerMsgLogDOMapper;
import com.example.rabbitmq_demo.resource.mapping.gen.OrderDOMapper;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class OrderService {

    @Autowired
    private OrderDOMapper orderDOMapper;

    @Autowired
    private BrokerMsgLogDOMapper brokerMsgLogDOMapper;

    @Autowired
    private RabbitOrderSender rabbitOrderSender;

    public void createOrder(OrderDO orderDO){
        //order current time
        Date orderTime = new Date();
        //order insert 业务数据入库
        orderDOMapper.insert(orderDO);
        //log insert 构建消息日志记录对象
        BrokerMsgLogDO brokerMsgLogDO = new BrokerMsgLogDO();
        brokerMsgLogDO.setMessageId(orderDO.getMessageId());;
        brokerMsgLogDO.setMessage(JSON.toJSONString(orderDO));
        brokerMsgLogDO.setTryCount(0);
        brokerMsgLogDO.setStatus("0"); //设置订单的发送状态为0表示发送中
        brokerMsgLogDO.setNextRetry(DateUtils.addMinutes(orderTime, Constants.ORDER_TIMEOUT));
        brokerMsgLogDO.setCreateTime(new Date());
        brokerMsgLogDO.setUpdateTime(new Date());
        brokerMsgLogDOMapper.insert(brokerMsgLogDO);
        //order message sender
        rabbitOrderSender.sendOrder(orderDO);
    }
}
