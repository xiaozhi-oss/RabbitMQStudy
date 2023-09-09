package com.example.confrimpublish.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

/**
 * @author xiaozhi
 */
@Slf4j
@Component
public class MyCallback implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnsCallback {

    /**
     * 消息回调，不管消息是否被确认都会进行回调
     * @param correlationData   消息的回调
     * @param ack               交换机是否接收到数据
     * @param cause             交换机未接收到消息的原因
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        String id = correlationData != null ? correlationData.getId() : "";
        if (ack) {
            log.info("交换机收到id为：{} 的消息", id);
        } else {
            log.info("交换机未收到 id为 {} 的消息，原因是：{}", id, cause);
        }
    }

    /**
     * 处理回退的消息
     * @param returnedMessage 回退消息的信息
     */
    @Override
    public void returnedMessage(ReturnedMessage returnedMessage) {
        String exchange = returnedMessage.getExchange();
        String routingKey = returnedMessage.getRoutingKey();
        String msg = new String(returnedMessage.getMessage().getBody(), StandardCharsets.UTF_8);
        String replyText = returnedMessage.getReplyText();
        log.info("消息：{} 被 {} 回退，回退理由是：{},RoutingKey为：{}", msg, exchange, replyText, routingKey);
    }
}
