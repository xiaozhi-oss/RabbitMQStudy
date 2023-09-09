package com.xiaozhi.client;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DeliverCallback;
import com.xiaozhi.utils.RabbitMQUtil;

import java.nio.charset.StandardCharsets;

/**
 * @author xiaozhi
 *
 * 线程消费者
 */
public class ThreadConsumer implements Runnable{

    @Override
    public void run(){
        String name = Thread.currentThread().getName();
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String msg = new String(delivery.getBody(), StandardCharsets.UTF_8);
            System.out.println("当前线程：" + name + "-> msg：" + msg);
        };
        CancelCallback cancelCallback = (consumerTag) -> {
            System.out.println("消息中断：" + consumerTag);
        };
        try {
            Connection con = RabbitMQUtil.getCon();
            Channel channel = con.createChannel();
            channel.basicConsume(RabbitMQUtil.QUEUE_NAME, true, deliverCallback, cancelCallback);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
