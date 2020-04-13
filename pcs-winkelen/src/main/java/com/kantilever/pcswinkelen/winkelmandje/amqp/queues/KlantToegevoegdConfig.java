package com.kantilever.pcswinkelen.winkelmandje.amqp.queues;

import com.gazellig.amqpservice.amqp.config.AuditableQueueConfig;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KlantToegevoegdConfig extends AuditableQueueConfig {

    @Value("${rabbitmq.topics.KlantToegevoegd}")
    private String topic;

    @Bean
    public Queue topicKlantToegevoegdQueue() {
        return new Queue(topic);
    }

    @Bean
    public Binding auditKlantToegevoegdBinding(TopicExchange auditExchange, Queue auditQueue) {
        return BindingBuilder.bind(auditQueue).to(auditExchange).with(topic);
    }

    @Bean
    public Binding topicKlantToegevoegdBinding(TopicExchange topicExchange,
                                                      Queue topicKlantToegevoegdQueue) {
        return BindingBuilder.bind(topicKlantToegevoegdQueue).to(topicExchange).with(topic);
    }
}
