package com.gazellig.amqpservice.amqp.receivers;

import com.gazellig.amqpservice.amqp.audit.receivers.AuditableEventReceiver;
import com.gazellig.amqpservice.amqp.events.AuditableEvent;

/**
 * EventReceiver is the base EventReceiver that all event Receivers have to implement.
 * <p>
 * In most cases if the service includes the need for an auditlog you want {@link AuditableEventReceiver}
 * as most Event that get received have to be read out from the auditlog as well.
 *
 * @param <T> Type of event to receive {@link AuditableEvent}
 */
public interface EventReceiver<T extends AuditableEvent> {
    /**
     * Receive an event. This class should be annotated with {@link org.springframework.amqp.rabbit.annotation.RabbitListener}
     * and should specify an id and set autoStartup to false.
     *
     * @param event Event to receive
     */
    void receive(T event);

}
