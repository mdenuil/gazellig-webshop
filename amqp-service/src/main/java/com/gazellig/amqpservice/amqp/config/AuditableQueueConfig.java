package com.gazellig.amqpservice.amqp.config;

import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * AuditableQueueConfig is the base for every configuration that declares Queues.
 * <p>
 * Define a String topic and create a Queue with the topic as name and then bind the Queue
 * top the available {@link TopicExchange}s topicExchange and auditExchange
 * <p>
 * Because beans either need a unique method name or Qualifier you're required to
 * implement the Queue and Bindings yourself.
 */
@Configuration
@Import({ExchangeConfig.class})
public abstract class AuditableQueueConfig {

}
