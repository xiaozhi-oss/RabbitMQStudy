package com.xiaozhi.study;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

/**
 * @author xiaozhi
 *
 * 消息生产者
 */
public class Product {

    public static final String QUEUE_NAME = "hello";

    public static void main(String[] args) throws IOException, TimeoutException {
        // 1 创建一个连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        // 设置连接信息
        factory.setHost("192.168.10.10");
        factory.setUsername("guest");
        factory.setPassword("guest");

        // 2 创建连接
        Connection connection = factory.newConnection();
        // 3 创建 channel
        Channel channel = connection.createChannel();

        /**
         * 生成一个队列 -> Channel#queueDeclare，参数如下：
         *      1.队列名称
         *      2.消息是否持久化
         *      3.消息是否共享，true可以多个消费者进行消费，false只能一个消费者进行消费
         *      4.队列是否自动删除，最后一个消费者消费完就进行删除
         *      5.其他参数
         */
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        /*
            发送一个消息 -> Channel#basicPublish，参数如下：
                1.发送到那个交换机
                2.路由的key是哪个
                3.其他的参数信息
                4.发送消息的消息体
         */
        String message = "hello word";
        channel.basicPublish("", QUEUE_NAME, null,message.getBytes(StandardCharsets.UTF_8));
        System.out.println("消息发送完毕");
    }
}
