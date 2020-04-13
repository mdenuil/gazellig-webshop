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
public class BestellingStatusBehandelbaarQueue extends AuditableQueueConfig {
    @Value("${rabbitmq.topics.BestellingStatusBehandelbaar}")
    private String topic;
    @Value("${rabbitmq.queue.identifier}")
    private String identifier;

    @Bean
    public Queue topicBestellingStatusBehandelbaarQueue() {
        return new Queue(topic + identifier);
    }

    @Bean
    public Binding auditBestellingStatusBehandelbaarBinding(TopicExchange auditExchange,
                                                            Queue auditQueue) {
        return BindingBuilder.bind(auditQueue).to(auditExchange).with(topic);
    }

    @Bean
    public Binding topicBestellingStatusBehandelbaarBinding(TopicExchange topicExchange,
                                                            Queue topicBestellingStatusBehandelbaarQueue) {
        return BindingBuilder.bind(topicBestellingStatusBehandelbaarQueue).to(topicExchange).with(topic);
    }

    String getTopic() {
        return topic;
    }

    String getIdentifier() {
        return identifier;
    }
}
