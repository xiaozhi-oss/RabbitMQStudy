package com.example.delayqueue.send;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author xiaozhi
 *
 * 消息生产者
 */
@RestController
public class Product {

    @Resource
    private RabbitTemplate rabbitTemplate;

    @GetMapping("/sendMsg")
    public void sendMsg() {

        rabbitTemplate.convertAndSend("X", "XA", "hello", message -> {
            message.getMessageProperties().setExpiration("1 000");
            return message;
        });
    }

}
