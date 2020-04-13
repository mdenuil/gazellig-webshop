package com.gazellig.amqpservice.amqp.senders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gazellig.amqpservice.amqp.audit.senders.AuditableEventSender;
import com.gazellig.amqpservice.amqp.events.AuditableEvent;

/**
 * EventSender is the base EventSender that all Senders have to implement.
 * <p>
 * In most cases if the service includes the need for an auditlog you want {@link AuditableEventSender}
 * as most Events that get send have to be read out by their receiving services from the auditlog as well.
 *
 * @param <T> Type of event to send {@link AuditableEvent}
 */
public interface EventSender<T extends AuditableEvent> {

    void send(T event) throws JsonProcessingException;
}
