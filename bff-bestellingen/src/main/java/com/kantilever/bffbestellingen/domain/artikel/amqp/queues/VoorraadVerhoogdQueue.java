package com.kantilever.bffbestellingen.domain.artikel.amqp.queues;

import com.gazellig.amqpservice.amqp.config.AuditableQueueConfig;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * VoorraadVerhoogdQueue configures the Queue for receiving VoorraadVerhoogd events and binds the topic to the audit
 * queue.
 */
@Configuration
public class VoorraadVerhoogdQueue extends AuditableQueueConfig {
    @Value("${rabbitmq.topics.VoorraadVerhoogd}")
    private String topic;

    @Value("${rabbitmq.queue.identifier}")
    private String identifier;

    @Bean
    public Queue topicVoorraadVerhoogdQueue() {
        return new Queue(topic + identifier);
    }

    @Bean
    public Binding auditVoorraadVerhoogdBinding(TopicExchange auditExchange, Queue auditQueue) {
        return BindingBuilder.bind(auditQueue).to(auditExchange).with(topic);
    }

    @Bean
    public Binding topicVoorraadVerhoogdBinding(TopicExchange topicExchange, Queue topicVoorraadVerhoogdQueue) {
        return BindingBuilder.bind(topicVoorraadVerhoogdQueue).to(topicExchange).with(topic);
    }

    String getTopic() {
        return topic;
    }

    String getIdentifier() {
        return identifier;
    }
}
