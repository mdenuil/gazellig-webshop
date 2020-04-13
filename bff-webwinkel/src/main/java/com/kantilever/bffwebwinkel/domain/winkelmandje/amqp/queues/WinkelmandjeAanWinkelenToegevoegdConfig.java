package com.kantilever.bffwebwinkel.domain.winkelmandje.amqp.queues;

import com.gazellig.amqpservice.amqp.config.AuditableQueueConfig;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WinkelmandjeAanWinkelenToegevoegdConfig extends AuditableQueueConfig {

    @Value("${rabbitmq.topics.WinkelmandjeAanWinkelenToegevoegd}")
    private String topic;

    @Value("${rabbitmq.queue.identifier}")
    private String identifier;

    @Bean
    public Queue topicWinkelmandjeAanWinkelenToegevoegdQueue() {
        return new Queue(topic + identifier);
    }

    @Bean
    public Binding auditWinkelmandjeAanWinkelenToegevoegdBinding(TopicExchange auditExchange, Queue auditQueue) {
        return BindingBuilder.bind(auditQueue).to(auditExchange).with(topic);
    }

    @Bean
    public Binding topicWinkelmandjeAanWinkelenToegevoegdBinding(TopicExchange topicExchange,
                                                     Queue topicWinkelmandjeAanWinkelenToegevoegdQueue) {
        return BindingBuilder.bind(topicWinkelmandjeAanWinkelenToegevoegdQueue).to(topicExchange).with(topic);
    }
}
