package com.kantilever.bffwebwinkel.domain.bestelling.amqp.queues;

import com.gazellig.amqpservice.amqp.config.AuditableQueueConfig;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * BestellingAanBestellenToegevoegdQueue is the queue configuraton for the BestellingAanBestellenToegevoegdEvent.
 * It binds the queue to the TopicExchange and creates a binding between the Auditlog Exchange and this topic.
 * <p>
 * This class extends {@link AuditableQueueConfig} to provide the Auditlog beans.
 * <p>
 * An identifier is needed when multiple services listen to the same queue
 */
@Configuration
public class BestellingAanBestellenToegevoegdQueue extends AuditableQueueConfig {

    @Value("${rabbitmq.topics.BestellingAanBestellenToegevoegd}")
    private String topic;

    @Value("${rabbitmq.queue.identifier}")
    private String identifier;

    @Bean
    public Queue topicBestellingToegevoegdQueue() {
        return new Queue(topic + identifier);
    }

    @Bean
    public Binding auditBestellingToegevoegdBinding(TopicExchange auditExchange, Queue auditQueue) {
        return BindingBuilder.bind(auditQueue).to(auditExchange).with(topic);
    }

    @Bean
    public Binding topicBestellingToegevoegdBinding(TopicExchange topicExchange, Queue topicBestellingToegevoegdQueue) {
        return BindingBuilder.bind(topicBestellingToegevoegdQueue).to(topicExchange).with(topic);
    }

    String getTopic() {
        return topic;
    }

    String getIdentifier() {
        return identifier;
    }
}
