package com.kantilever.bffwebwinkel.domain.klant.amqp.queues;

import com.gazellig.amqpservice.amqp.config.AuditableQueueConfig;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * KlantToegevoegdConfig is the queue configuraton for the KlantToegevoegdEvent. It binds the queue to the
 * TopicExchange and creates a binding between the Auditlog Exchange and this topic.
 * <p>
 * This class extends {@link AuditableQueueConfig} to provide the Auditlog beans.
 */
@Configuration
public class KlantToegevoegdQueue extends AuditableQueueConfig {
    @Value("${rabbitmq.topics.KlantToegevoegdEvent}")
    public String topic;

    @Value("${rabbitmq.queue.identifier}")
    private String identifier;

    @Bean
    public Queue topicKlantToegevoegdEventQueue() {
        return new Queue(topic + identifier);
    }

    @Bean
    public Binding auditKlantToegevoegdEventBinding(TopicExchange auditExchange, Queue auditQueue) {
        return BindingBuilder.bind(auditQueue).to(auditExchange).with(topic);
    }

    @Bean
    public Binding topicKlantToegevoegdEventBinding(TopicExchange topicExchange, Queue topicKlantToegevoegdEventQueue) {
        return BindingBuilder.bind(topicKlantToegevoegdEventQueue).to(topicExchange).with(topic);
    }

    String getTopic() {
        return topic;
    }
}
