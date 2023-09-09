package com.example.confrimpublish.product;

import com.example.confrimpublish.config.ConfirmPublishConfig;
import com.example.confrimpublish.config.MyCallback;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * @author xiaozhi
 */
@Slf4j
@RestController
public class ConfirmProduct {

    @Resource
    private RabbitTemplate rabbitTemplate;

    @Resource
    private MyCallback myCallback;

    @PostConstruct
    public void init() {
        rabbitTemplate.setConfirmCallback(myCallback);
        rabbitTemplate.setMandatory(true);
        // 设置处理回调消息
        rabbitTemplate.setReturnsCallback(myCallback);
    }

    @GetMapping("/sendMsg/{msg}")
    public void sendMsg(@PathVariable("msg") String msg) {
        String msg1 = msg + ConfirmPublishConfig.ROUTING_KEY;
        String msg2 = msg + ConfirmPublishConfig.ROUTING_KEY1;

        // 发送第一条消息
        CorrelationData correlationData1 = new CorrelationData("1");        // 设置消息id
        rabbitTemplate.convertAndSend(ConfirmPublishConfig.CONFIRM_EXCHANGE,
                ConfirmPublishConfig.ROUTING_KEY, msg1, correlationData1);
        log.info("发送消息：{}", msg1);

        // 发送第二条消息
        CorrelationData correlationData2 = new CorrelationData("2");        // 设置消息id
        rabbitTemplate.convertAndSend(ConfirmPublishConfig.CONFIRM_EXCHANGE,
                ConfirmPublishConfig.ROUTING_KEY1, msg2, correlationData2);
        log.info("发送消息：{}", msg2);
    }

}
