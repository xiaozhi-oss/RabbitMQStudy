package com.example.deadqueue.controller;

import com.example.deadqueue.config.RabbitMQConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author xiaozhi
 */
@Slf4j
@RestController
public class SendMsgController {

    @Resource
    private RabbitTemplate rabbitTemplate;

    @GetMapping("/ttl/{msg}")
    public void acceptInput(@PathVariable("msg") String msg) {
        msg = "来自 xiaozhi 的消息：" + msg;
        MessageProperties properties = new MessageProperties();
        // 设置过期时间
        properties.setExpiration("3000");
        Message message = new Message(msg.getBytes(), properties);
        rabbitTemplate.convertAndSend(RabbitMQConfig.X_EXCHANGE, "XA", message);
        log.info("时间：{}, 发送消息：{}", new Date(), msg);
    }

    @GetMapping("/maxLen")
    public void acceptInput2() {
        for (int i = 0; i < 6; i++) {
            rabbitTemplate.convertAndSend(RabbitMQConfig.X_EXCHANGE, "XA", String.valueOf(i));
            log.info("发送第 {} 条消息", i);
        }
    }

    @GetMapping("/sendMsg")
    public void acceptInput3() {
        rabbitTemplate.convertAndSend(RabbitMQConfig.X_EXCHANGE, "XA", "哈哈");
        log.info("生产者发送消息");
    }
}
