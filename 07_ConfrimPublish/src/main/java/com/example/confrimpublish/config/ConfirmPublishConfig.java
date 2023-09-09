package com.example.confrimpublish.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author xiaozhi
 */
@Configuration
public class ConfirmPublishConfig {

    public static final String CONFIRM_EXCHANGE = "confirm.exchange";
    public static final String BACKUP_EXCHANGE = "backup.exchange";
    public static final String CONFIRM_QUEUE = "confirm.queue";
    public static final String BACKUP_QUEUE = "backup.queue";
    public static final String WARNING_QUEUE = "warning.queue";
    public static final String ROUTING_KEY = "key";
    public static final String ROUTING_KEY1 = "key1";

    @Bean(CONFIRM_EXCHANGE)
    public Exchange directExchange() {
        return ExchangeBuilder.directExchange(CONFIRM_EXCHANGE)
                .durable(true)
                .withArgument("alternate-exchange", BACKUP_EXCHANGE)
                .build();
    }

    @Bean(BACKUP_EXCHANGE)
    public FanoutExchange fanoutExchange() {
        return new FanoutExchange(BACKUP_EXCHANGE);
    }

    @Bean(CONFIRM_QUEUE)
    public Queue queue() {
        return QueueBuilder.durable(CONFIRM_QUEUE).build();
    }

    @Bean(BACKUP_QUEUE)
    public Queue backupQueue() {
        return QueueBuilder.durable(BACKUP_QUEUE).build();
    }

    @Bean(WARNING_QUEUE)
    public Queue warningQueue() {
        return QueueBuilder.durable(WARNING_QUEUE).build();
    }

    @Bean
    public Binding bindingKey1(@Qualifier(CONFIRM_EXCHANGE) Exchange exchange,
                           @Qualifier(CONFIRM_QUEUE) Queue queue) {
        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY).noargs();
    }

    @Bean
    public Binding bindingBackup(@Qualifier(BACKUP_EXCHANGE) FanoutExchange exchange,
                           @Qualifier(BACKUP_QUEUE) Queue queue) {
        return BindingBuilder.bind(queue).to(exchange);
    }

    @Bean
    public Binding bindingWarning(@Qualifier(BACKUP_EXCHANGE) FanoutExchange exchange,
                           @Qualifier(WARNING_QUEUE) Queue queue) {
        return BindingBuilder.bind(queue).to(exchange);
    }
}
