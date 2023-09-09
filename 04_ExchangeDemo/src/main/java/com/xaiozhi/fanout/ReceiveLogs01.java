package com.xaiozhi.fanout;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.xaiozhi.util.RabbitMQUtil;

import java.nio.charset.StandardCharsets;

/**
 * @author xiaozhi
 */
public class ReceiveLogs01 {

    private static final String EXCHANGE_NAME = "logs";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMQUtil.getChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.FANOUT);
        // 生成一个随机队列，当消费者断开和该队列的连接时就会进行删除
        String queueName = channel.queueDeclare().getQueue();
        channel.queueBind(queueName, EXCHANGE_NAME, "");
        System.out.println("c1 等待消息...");
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println("消息：" + new String(message.getBody(), StandardCharsets.UTF_8));
        };
        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {});
    }
}
