package com.xiaozhi.study;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

/**
 * @author xiaozhi
 *
 * 消息消费者
 */
public class Consumer {

    public static final String QUEUE_NAME = "hello";

    public static void main(String[] args) throws IOException, TimeoutException {
        // 1 创建一个连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        // 设置连接信息
        factory.setHost("192.168.10.10");
        factory.setUsername("guest");
        factory.setPassword("guest");

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        System.out.println("等待接收消息...");

        // 接收消息成功的回调
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            System.out.println(consumerTag);
            System.out.println(new String(delivery.getBody(), StandardCharsets.UTF_8));
        };

        // 取消消费的一个回调接口 如在消费的时候队列被删除掉了
        CancelCallback cancelCallback = (consumerTag) -> {
            System.out.println("消息中断");
        };

        /*
            消费者消费消息 -> channel#basicConsume
                1.消费的队列名
                2.消费完成是否自动应答，ture表示自动应答，false表示手动应答
                3.消息消费成功回调
                4.消费消费失败回调
         */
        channel.basicConsume(QUEUE_NAME, true, deliverCallback, cancelCallback);
    }
}
