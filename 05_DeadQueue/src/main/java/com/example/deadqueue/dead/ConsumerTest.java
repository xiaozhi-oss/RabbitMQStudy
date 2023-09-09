package com.example.deadqueue.dead;


import com.example.deadqueue.config.RabbitMQConfig;
import com.rabbitmq.client.Channel;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

/**
  * @author xiaozhi
 *
 *  消费者
  */
@Slf4j
@Component
public class ConsumerTest {

    @SneakyThrows
    @RabbitListener(queues = { RabbitMQConfig.QUEUE_A })
    public void receiveD(Message message, Channel channel) {
        // 获取消费队列名
        String queueName = message.getMessageProperties().getConsumerQueue();
        channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
        log.info("队列 {} 拒绝消息：{}", queueName, new String(message.getBody(), StandardCharsets.UTF_8));
    }

}
