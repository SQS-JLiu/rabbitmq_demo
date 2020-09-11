use mydb;
-- 表order订单结构
CREATE TABLE IF NOT EXISTS `t_order`(
    `id` varchar(128) NOT NULL COMMENT "订单ID",
    `name` varchar(128) COMMENT "订单名称",
    `message_id` varchar(128) NOT NULL COMMENT "消息唯一ID",
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 表broker_message_log 消息记录结构
CREATE TABLE IF NOT EXISTS `broker_message_log` (
    `message_id` varchar(128) NOT NULL COMMENT "消息唯一ID",
    `message` varchar(4000) DEFAULT NULL COMMENT "消息内容",
    `try_count` int(4) DEFAULT 0 COMMENT "重复次数",
    `status` varchar(10) DEFAULT '' COMMENT "消息投递状态 0投递中 1投递成功 2投递失败",
    `next_retry` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT "下一次重新投递的时间或超时时间",
    `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT "创建时间",
    `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT "更新时间",
    PRIMARY KEY (`message_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;