package com.example.rabbitmq_demo.task;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.rabbitmq_demo.constant.Constants;
import com.example.rabbitmq_demo.controller.RabbitOrderSender;
import com.example.rabbitmq_demo.resource.domain.gen.BrokerMsgLogDO;
import com.example.rabbitmq_demo.resource.domain.gen.OrderDO;
import com.example.rabbitmq_demo.resource.mapping.gen.BrokerMsgLogDOMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class RetryMessageTasker {
    @Autowired
    private RabbitOrderSender rabbitOrderSender;

    @Autowired
    private BrokerMsgLogDOMapper brokerMsgLogDOMapper;

    @Scheduled(initialDelay = 3000,fixedDelay = 10000)
    public void reSend(){
        System.out.println("-------------定时任务开始------------");
        //pull status=0 and timeout message
        List<BrokerMsgLogDO> list = brokerMsgLogDOMapper.queryByStatusAndTimeoutMessage();
        list.forEach(brokerMsgLogDO -> {
            if(brokerMsgLogDO.getTryCount() >= 3){
                //update fail message
                brokerMsgLogDOMapper.changeBrokerMessageLogStatus(brokerMsgLogDO.getMessageId(), Constants.ORDER_SEND_FAILURE,new Date());
            }else {
                //resend
                brokerMsgLogDOMapper.update4Resend(brokerMsgLogDO.getMessageId(),new Date());
                OrderDO reSendOrder = JSON.toJavaObject((JSON) JSON.parse(brokerMsgLogDO.getMessage()),OrderDO.class);
                try{
                    rabbitOrderSender.sendOrder(reSendOrder);
                }catch (Exception e){
                    e.printStackTrace();
                    System.err.println("-------------异常处理--------------");
                }
            }
        });
    }
}
