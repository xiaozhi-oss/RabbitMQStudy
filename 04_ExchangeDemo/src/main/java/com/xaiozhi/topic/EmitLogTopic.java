package com.xaiozhi.topic;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.xaiozhi.util.RabbitMQUtil;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @author xiaozhi
 */
public class EmitLogTopic {

    private static final String EXCHANGE_NAME = "topic_logs";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMQUtil.getChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);
        Map<String, String> bindingMap = new HashMap<>();
        bindingMap.put("quick.orange.rabbit","被队列 Q1Q2 接收到");
        bindingMap.put("lazy.orange.elephant","被队列 Q1Q2 接收到");
        bindingMap.put("quick.orange.fox","被队列 Q1 接收到");
        bindingMap.put("lazy.brown.fox","被队列 Q2 接收到");
        bindingMap.put("lazy.pink.rabbit","虽然满足两个绑定但只被队列 Q2 接收一次");
        bindingMap.put("quick.brown.fox","不匹配任何绑定不会被任何队列接收到会被丢弃");
        bindingMap.put("quick.orange.male.rabbit","是四个单词不匹配任何绑定会被丢弃");
        bindingMap.put("lazy.orange.male.rabbit","是四个单词但匹配 Q2");
        bindingMap.forEach((k, v) -> {
            try {
                channel.basicPublish(EXCHANGE_NAME, k, null, v.getBytes(StandardCharsets.UTF_8));
                System.out.println("发送消息：" + v);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
