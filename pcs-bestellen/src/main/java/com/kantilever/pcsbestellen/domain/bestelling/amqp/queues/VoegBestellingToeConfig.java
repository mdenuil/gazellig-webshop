package com.kantilever.pcsbestellen.domain.bestelling.amqp.queues;

import com.gazellig.amqpservice.amqp.config.AuditableQueueConfig;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for the queues that have to do with Bestellingen. This class imports QueueConfig which is the base for
 * all classes that configure a queue.
 * <p>
 * When adding new topics and binding them to a queue, make sure to also create a binding to the auditQueue to ensure
 * everything is read from the AuditLog.
 */
@Configuration
public class VoegBestellingToeConfig extends AuditableQueueConfig {
    @Value("${rabbitmq.topics.BestellingGeplaatstEvent}")
    private String topic;

    @Value("${rabbitmq.queue.identifier}")
    private String identifier;

    @Bean
    public Queue topicBestellingGeplaatstQueue() {
        return new Queue(topic + identifier);
    }

    @Bean
    public Binding auditBestellingGeplaatstBinding(TopicExchange auditExchange, Queue auditQueue) {
        return BindingBuilder.bind(auditQueue).to(auditExchange).with(topic);
    }

    @Bean
    public Binding topicBestellingGeplaatstBinding(TopicExchange topicExchange,
                                                   Queue topicBestellingGeplaatstQueue) {
        return BindingBuilder.bind(topicBestellingGeplaatstQueue).to(topicExchange).with(topic);
    }
}
