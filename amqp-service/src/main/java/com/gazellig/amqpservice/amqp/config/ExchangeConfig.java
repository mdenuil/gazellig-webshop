package com.gazellig.amqpservice.amqp.config;

import org.springframework.amqp.core.AnonymousQueue;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration base for every {@link AuditableQueueConfig} configuration.
 */
@Configuration
public class ExchangeConfig {
    @Value("${rabbitmq.event.exchange}")
    private String topicExchangeName;

    @Value("${auditlog.replay.exchange}")
    private String auditExchangeName;

    @Bean
    public Queue auditQueue() {
        return new AnonymousQueue();
    }

    @Bean
    public TopicExchange auditExchange() {
        return new TopicExchange(auditExchangeName, false, false);
    }

    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange(topicExchangeName, false, false);
    }
}
