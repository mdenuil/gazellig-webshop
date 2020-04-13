package com.kantilever.pcsbestellen.domain.artikel.amqp.queues;

import com.gazellig.amqpservice.amqp.config.AuditableQueueConfig;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ArtikelToegevoegdConfig extends AuditableQueueConfig {
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
}
