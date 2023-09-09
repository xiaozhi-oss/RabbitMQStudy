package com.example.delayqueue.controller;

import com.example.delayqueue.config.DelayedQueueConfig;
import lombok.extern.slf4j.Slf4j;
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
public class DelayedController {

    @Resource
    private RabbitTemplate rabbitTemplate;

    @GetMapping("delayed/{msg}/{ttl}")
    public void sendMsg(@PathVariable("msg") String msg,
                        @PathVariable("ttl") Integer ttl) {
        rabbitTemplate.convertAndSend(DelayedQueueConfig.DELAYED_EXCHANGE_NAME, DelayedQueueConfig.DELAYED_ROUTING_KEY, msg, message -> {
            // 设置延迟时间
            message.getMessageProperties().setDelay(ttl * 1000);
            return message;
        });
        log.info("当前时间为：{}，发送一个 ttl 为 {}秒 的消息：{}",new Date(), ttl, msg);
    }
}
