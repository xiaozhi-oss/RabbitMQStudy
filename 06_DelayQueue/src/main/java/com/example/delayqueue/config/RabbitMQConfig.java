package com.example.delayqueue.config;

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
    public static final String QUEUE_B = "QB";
    public static final String QUEUE_C = "QC";
    public static final String DEAD_LETTER_QUEUE = "QD";
    public static final String QA_X_ROUTING_KEY = "XA";
    public static final String QB_X_ROUTING_KEY = "XB";
    public static final String QC_X_ROUTING_KEY = "XC";
    public static final String DEAD_ROUTING_KEY = "YD";


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
        params.put("x-dead-letter-routing-key", DEAD_ROUTING_KEY);
        // 设置队列的TTL
        params.put("x-message-ttl", 3000);
        return QueueBuilder.durable(QUEUE_A).withArguments(params).build();
    }

    @Bean("queueB")
    public Queue queueB() {
        Map<String, Object> params = new HashMap<>();
        // 声明当前队列绑定死信交换机
        params.put("x-dead-letter-exchange", Y_DEAD_LETTER_EXCHANGE);
        // 声明当前队列的死信路由key
        params.put("x-dead-letter-routing-key", DEAD_ROUTING_KEY);
        // 设置队列的TTL
        params.put("x-message-ttl", 5000);
        return QueueBuilder.durable(QUEUE_B).withArguments(params).build();
    }

    @Bean("queueC")
    public Queue queueC() {
        Map<String, Object> params = new HashMap<>();
        // 声明当前队列绑定死信交换机
        params.put("x-dead-letter-exchange", Y_DEAD_LETTER_EXCHANGE);
        // 声明当前队列的死信路由key
        params.put("x-dead-letter-routing-key", DEAD_ROUTING_KEY);
        return QueueBuilder.durable(QUEUE_C).withArguments(params).build();
    }

    // 声明死信队列
    @Bean("queueD")
    public Queue queueD() {
        return new Queue(DEAD_LETTER_QUEUE);
    }

    // 声明队列A绑定X交换机
    @Bean
    public Binding qaBindingX(@Qualifier("queueA") Queue queueA,
                              @Qualifier("xExchange") DirectExchange xExchange) {
        return BindingBuilder.bind(queueA).to(xExchange).with(QA_X_ROUTING_KEY);
    }

    // 声明队列A绑定X交换机
    @Bean
    public Binding qbBindingX(@Qualifier("queueB") Queue queueB,
                              @Qualifier("xExchange") DirectExchange xExchange) {
        return BindingBuilder.bind(queueB).to(xExchange).with(QB_X_ROUTING_KEY);
    }

    @Bean
    public Binding qcBindingX(@Qualifier("queueC") Queue queueC,
                              @Qualifier("xExchange") DirectExchange xExchange) {
        return BindingBuilder.bind(queueC).to(xExchange).with(QC_X_ROUTING_KEY);
    }

    // 给死信交换机绑定队列
    @Bean
    public Binding queueBindingD(@Qualifier("queueD") Queue queueD,
                                 @Qualifier("yExchange") DirectExchange yExchange) {
        return BindingBuilder.bind(queueD).to(yExchange).with(DEAD_ROUTING_KEY);
    }
}
