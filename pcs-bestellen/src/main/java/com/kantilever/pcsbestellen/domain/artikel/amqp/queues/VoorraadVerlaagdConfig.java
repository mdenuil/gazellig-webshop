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
public class VoorraadVerlaagdConfig extends AuditableQueueConfig {
    @Value("${rabbitmq.topics.VoorraadVerlaagd}")
    private String topic;

    @Value("${rabbitmq.queue.identifier}")
    private String identifier;

    @Bean
    public Queue topicVoorraadVerlaagdQueue() {
        return new Queue(topic + identifier);
    }

    @Bean
    public Binding auditVoorraadVerlaagdBinding(TopicExchange auditExchange, Queue auditQueue) {
        return BindingBuilder.bind(auditQueue).to(auditExchange).with(topic);
    }

    @Bean
    public Binding topicVoorraadVerlaagdBinding(TopicExchange topicExchange, Queue topicVoorraadVerlaagdQueue) {
        return BindingBuilder.bind(topicVoorraadVerlaagdQueue).to(topicExchange).with(topic);
    }
}
