package com.example.rabbitmq_demo.entity;

import java.io.Serializable;
import java.util.UUID;

public class Order implements Serializable {

    private String id;

    private String name;

    private String messageId; //存储消息发送的唯一标识

    public Order(){
        this.id = "1";
        this.name = "zhangsan";
        messageId = System.currentTimeMillis()+"$"+ UUID.randomUUID().toString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }
}
