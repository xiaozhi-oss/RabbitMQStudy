package com.xaiozhi.topic;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.xaiozhi.util.RabbitMQUtil;

import java.nio.charset.StandardCharsets;

/**
 * @author xiaozhi
 */
public class ReceiveLogsTopic02 {

    private static final String EXCHANGE_NAME = "topic_logs";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMQUtil.getChannel();
        String queueName = "Q2";
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);
        channel.queueDeclare(queueName, false, false, false, null);
        channel.queueBind(queueName, EXCHANGE_NAME, "*.*.rabbit");
        channel.queueBind(queueName, EXCHANGE_NAME, "lazy.#");
        System.out.println("c2（RoutingKey=*.*.rabbit & lazy.#）等待接收消息...");
        DeliverCallback deliverCallback  = ((consumerTag, message) -> {
            String msg = new String(message.getBody(), StandardCharsets.UTF_8);
            String routingKey = message.getEnvelope().getRoutingKey();
            System.out.println("绑定键：" + routingKey + "\t接收到消息：" + msg);
        });
        channel.basicConsume(queueName, false, deliverCallback, consumerTag -> {});
    }
}
