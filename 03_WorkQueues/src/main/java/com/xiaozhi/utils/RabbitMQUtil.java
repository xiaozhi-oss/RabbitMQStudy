package com.xiaozhi.utils;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * @author xiaozhi
 *
 * 连接 RabbitMQ 工具类
 */
public class RabbitMQUtil {

    public static final String QUEUE_NAME = "hello";

    public static Channel getChannel() throws Exception {
        return getCon().createChannel();
    }

    public static Connection getCon() throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        // 设置连接信息
        factory.setHost("rabbitmq-study");
        factory.setUsername("guest");
        factory.setPassword("guest");
        return factory.newConnection();
    }
}
