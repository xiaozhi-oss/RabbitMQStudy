package com.xaiozhi.topic;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.xaiozhi.util.RabbitMQUtil;

import java.nio.charset.StandardCharsets;

/**
 * @author xiaozhi
 */
public class ReceiveLogsTopic01 {

    private static final String EXCHANGE_NAME = "topic_logs";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMQUtil.getChannel();
        String queueName = "Q1";
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);
        channel.queueDeclare(queueName, false, false, false, null);
        channel.queueBind(queueName, EXCHANGE_NAME, "*.orange.*");
        System.out.println("c1（RoutingKey=*.orange.*）等待接收消息...");
        DeliverCallback deliverCallback  = ((consumerTag, message) -> {
            String msg = new String(message.getBody(), StandardCharsets.UTF_8);
            String routingKey = message.getEnvelope().getRoutingKey();
            System.out.println("绑定键：" + routingKey + "\t接收到消息：" + msg);
        });
        channel.basicConsume(queueName, false, deliverCallback, consumerTag -> {});
    }
}
