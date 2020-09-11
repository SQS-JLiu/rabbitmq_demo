package com.example.rabbitmq_demo.resource.mapping.gen;

import com.example.rabbitmq_demo.resource.domain.gen.BrokerMsgLogDO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface BrokerMsgLogDOMapper {
    int deleteByPrimaryKey(String messageId);

    int insert(BrokerMsgLogDO record);

    int insertSelective(BrokerMsgLogDO record);

    BrokerMsgLogDO selectByPrimaryKey(String messageId);

    int updateByPrimaryKeySelective(BrokerMsgLogDO record);

    int updateByPrimaryKey(BrokerMsgLogDO record);

    int changeBrokerMessageLogStatus(@Param("msgId")String msgId, @Param("status")String status, @Param("updateTime")Date date);

    int update4Resend(@Param("msgId")String msgId, @Param("updateTime")Date date);
    /**
     * 查询消息状态为0(发送中) 且已超时的消息集合
     * @return
     */
    List<BrokerMsgLogDO> queryByStatusAndTimeoutMessage();
}