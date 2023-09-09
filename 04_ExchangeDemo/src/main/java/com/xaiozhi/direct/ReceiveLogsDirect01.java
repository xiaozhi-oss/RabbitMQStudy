package com.xaiozhi.direct;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.xaiozhi.util.RabbitMQUtil;

import java.nio.charset.StandardCharsets;

/**
 * @author xiaozhi
 */
public class ReceiveLogsDirect01 {

    // 交换机名称
    public static final String EXCHANGE_NAME = "direct_logs";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMQUtil.getChannel();
        // 创建交换机
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        // 创建队列
        String queueName = "console";
        channel.queueDeclare(queueName, false, false, false, null);
        // 绑定交换机
        channel.queueBind(queueName, EXCHANGE_NAME, "info");
        channel.queueBind(queueName, EXCHANGE_NAME, "warning");
        System.out.println("等待接收消息");
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String msg = new String(delivery.getBody(), StandardCharsets.UTF_8);
            System.out.println("绑定建：" + delivery.getEnvelope().getRoutingKey() + "消息：" + msg);
        };
        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {});
    }
}
