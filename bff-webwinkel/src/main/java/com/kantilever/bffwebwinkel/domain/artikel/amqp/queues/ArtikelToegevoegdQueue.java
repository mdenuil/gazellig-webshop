package com.kantilever.bffwebwinkel.domain.artikel.amqp.queues;

import com.gazellig.amqpservice.amqp.config.AuditableQueueConfig;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * ArtikelToegevoegdQueue configures the Queue for receiving ArtikelToegevoegd events and binds the topic to the audit
 * queue.
 */
@Configuration
public class ArtikelToegevoegdQueue extends AuditableQueueConfig {
    @Value("${rabbitmq.topics.ArtikelAanCatalogusToegevoegd}")
    private String topic;

    @Value("${rabbitmq.queue.identifier}")
    private String identifier;

    @Bean
    public Queue topicArtikelToegevoegdQueue() {
        return new Queue(topic + identifier);
    }

    @Bean
    public Binding auditArtikelToegevoegdBinding(TopicExchange auditExchange, Queue auditQueue) {
        return BindingBuilder.bind(auditQueue).to(auditExchange).with(topic);
    }

    @Bean
    public Binding topicArtikelToegevoegdBinding(TopicExchange topicExchange, Queue topicArtikelToegevoegdQueue) {
        return BindingBuilder.bind(topicArtikelToegevoegdQueue).to(topicExchange).with(topic);
    }

    String getTopic() {
        return topic;
    }

    String getIdentifier() {
        return identifier;
    }
}
