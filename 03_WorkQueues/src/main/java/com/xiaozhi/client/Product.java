package com.xiaozhi.client;

import com.rabbitmq.client.Channel;
import com.xiaozhi.utils.RabbitMQUtil;
import com.xiaozhi.utils.SleepUtil;

import java.nio.charset.StandardCharsets;

/**
 * @author xiaozhi
 *
 * 消息生产者，生产九条消息
 */
public class Product {

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMQUtil.getChannel();
        channel.queueDeclare(RabbitMQUtil.QUEUE_NAME, false, false, false, null);
        for (int i = 0; i < 1000000; i++) {
            String msg = String.valueOf(i);
            System.out.println("消息发送完毕：" + i);
            channel.basicPublish("", RabbitMQUtil.QUEUE_NAME, null, msg.getBytes(StandardCharsets.UTF_8));
        }
    }
}
