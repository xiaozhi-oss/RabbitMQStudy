package com.example.delayqueue.dead;

import com.example.delayqueue.config.DelayedQueueConfig;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * @author xiaozhi
 */
@Slf4j
@Component
public class DelayedConsumer {

    // 这边这需要消费死信队列中的消息即可
    @RabbitListener(queues = { DelayedQueueConfig.DELAYED_QUEUE_NAME })
    public void receiveD(Message message, Channel channel) {
        // 获取第一个接收消息的队列
        String msg = new String(message.getBody(), StandardCharsets.UTF_8);
        log.info("当前时间为：{}, 接收消息：{}", new Date(), msg);
    }

}
