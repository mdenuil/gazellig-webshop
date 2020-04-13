package com.gazellig.amqpservice.amqp.audit.senders;


import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gazellig.amqpservice.amqp.events.AuditableEvent;
import com.gazellig.amqpservice.amqp.senders.EventSender;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

/**
 * AuditableEventSender sends an event fit for consumption by the AuditLog. Everything Except the Topic to send on
 * is preconfigured. Simply extend this class and inject the right topic + override getTopic().
 * <p>
 * Because the AuditLog is insane it encodes messages in UTF16LE. Sending default messages with
 * UTF8 results in chinese characters and unreadable JSON.
 * <p>
 * If you want to send messages that can be received and processed during replay extend this class.
 *
 * @param <T> Type of event to send {@link AuditableEvent}
 */
@Log4j2
public abstract class AuditableEventSender<T extends AuditableEvent> implements EventSender<T> {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private ObjectMapper objectMapper;
    @Value("${rabbitmq.event.exchange}")
    private String exchange;

    @Override
    public void send(T event) throws JsonProcessingException {
        rabbitTemplate.send(exchange, event.getTopic(), buildMessage(event));
        log.info(String.format("Event send on topic: %s. Event: %s", event.getTopic(), event));
    }

    private Message buildMessage(T event) throws JsonProcessingException {
        return MessageBuilder
                .withBody(objectMapper.writeValueAsString(event).getBytes(StandardCharsets.UTF_16LE))
                .setContentType(MessageProperties.CONTENT_TYPE_JSON)
                .setType(event.getTopic())
                .setReceivedExchange(exchange)
                .setCorrelationId(event.getCorrelationId().toString())
                .setTimestamp(Date.from(Instant.now()))
                .build();
    }
}
