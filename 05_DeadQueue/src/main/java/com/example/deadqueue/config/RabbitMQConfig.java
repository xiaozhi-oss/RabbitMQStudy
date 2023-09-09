package com.example.deadqueue.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
  * 配置交换机和队列
  *
  * @author xiaozhi
  */
@Configuration
public class RabbitMQConfig {

    public static final String X_EXCHANGE = "X";
    public static final String Y_DEAD_LETTER_EXCHANGE = "Y";
    public static final String QUEUE_A = "QA";
    public static final String DEAD_LETTER_QUEUE = "QD";

    @Bean("xExchange")
    public DirectExchange xExchange() {
        return new DirectExchange(X_EXCHANGE);
    }

    @Bean("yExchange")
    public DirectExchange yExchange() {
        return new DirectExchange(Y_DEAD_LETTER_EXCHANGE);
    }

    @Bean("queueA")
    public Queue queueA() {
        Map<String, Object> params = new HashMap<>();
        // 声明当前队列绑定死信交换机
        params.put("x-dead-letter-exchange", Y_DEAD_LETTER_EXCHANGE);
        // 声明当前队列的死信路由key
        params.put("x-dead-letter-routing-key", "YD");
        return QueueBuilder.durable(QUEUE_A).withArguments(params).build();
    }

    // 声明队列A绑定X交换机
    @Bean
    public Binding queryBindingX(@Qualifier("queueA") Queue queueA,
                                 @Qualifier("xExchange") DirectExchange xExchange) {
        return BindingBuilder.bind(queueA).to(xExchange).with("XA");
    }

    // 声明死信队列
    @Bean("queueD")
    public Queue queueD() {
        return new Queue(DEAD_LETTER_QUEUE);
    }

    // 给死信交换机绑定队列
    @Bean
    public Binding queueBindingD(@Qualifier("queueD") Queue queueD,
                                 @Qualifier("yExchange") DirectExchange yExchange) {
        return BindingBuilder.bind(queueD).to(yExchange).with("YD");
    }
}
