package com.example.confrimpublish.consumer;

import com.example.confrimpublish.config.ConfirmPublishConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

/**
 * @author xiaozhi
 */
@Slf4j
@Component
public class WarningConsumer {

    @RabbitListener(queues = { ConfirmPublishConfig.WARNING_QUEUE })
    public void receiveMsg(Message message){
        String msg = new String(message.getBody(), StandardCharsets.UTF_8);
        log.info("报警发现不可路由消息：{}", msg);
    }
}
