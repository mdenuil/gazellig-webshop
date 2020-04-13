package com.kantilever.bffbestellingen.domain.bestelling.amqp.queues;

import com.gazellig.amqpservice.amqp.config.AuditableQueueConfig;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BestellingStatusVerstuurdQueue  extends AuditableQueueConfig {
    @Value("${rabbitmq.topics.BestellingStatusVerstuurd}")
    private String topic;

    @Value("${rabbitmq.queue.identifier}")
    private String identifier;

    @Bean
    public Queue topicBestellingStatusVerstuurdQueue() {
        return new Queue(topic + identifier);
    }

    @Bean
    public Binding auditBestellingStatusVerstuurdBinding(TopicExchange auditExchange, Queue auditQueue) {
        return BindingBuilder.bind(auditQueue).to(auditExchange).with(topic);
    }

    @Bean
    public Binding topicBestellingStatusVerstuurdBinding(TopicExchange topicExchange, Queue topicBestellingStatusVerstuurdQueue) {
        return BindingBuilder.bind(topicBestellingStatusVerstuurdQueue).to(topicExchange).with(topic);
    }

    String getTopic() {
        return topic;
    }

    String getIdentifier() {
        return identifier;
    }
}
