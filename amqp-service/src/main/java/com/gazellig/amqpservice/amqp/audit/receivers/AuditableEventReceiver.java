package com.gazellig.amqpservice.amqp.audit.receivers;

import javax.annotation.PostConstruct;
import com.gazellig.amqpservice.amqp.audit.handledevents.HandledEventService;
import com.gazellig.amqpservice.amqp.audit.replay.AuditReplayEventReceiver;
import com.gazellig.amqpservice.amqp.events.AuditableEvent;
import com.gazellig.amqpservice.amqp.receivers.EventReceiver;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * AuditableEventReceiver receives an event and register themselves as an event receiver
 * which has to be processed by the Auditlog replay.
 * <p>
 * In most cases this is the class you want to extend for a receiver, as most events needed by a
 * also service also first need to have their audit logs entries read.
 * <p>
 * In order for events to be processed by the AuditReplayQueue a binding has to be made between the
 * AuditExchange and the Queues topic. If you extends this class to receive messages from a new
 * queue you also want to create a binding to  the AuditExchange when creating your normal binding.
 *
 * @param <T> Type of event to receive {@link AuditableEvent}
 */
public abstract class AuditableEventReceiver<T extends AuditableEvent> implements EventReceiver<T> {
    @Autowired
    protected HandledEventService handledEventService;
    @Autowired
    protected AuditReplayEventReceiver auditReplayEventReceiver;

    /**
     * AuditableEvents have to have their topic in order for the AuditLog to find the right receiver
     * for a specific event.
     *
     * @return the topic of the receiver
     */
    public abstract String getTopic();

    public int getPriority() {
        return 100;
    }

    /**
     * All AuditableReceivers have to register themselves with the AuditReplayEventReceiver in order for the Auditlog
     * of the events to be read prior to the receiver being started.
     * <p>
     * This function is automatically called after the constructor is done initializing to ensure the topic is initialized
     * correctly in case of autowiring.
     */
    @PostConstruct
    protected void registerWithAudit() {
        auditReplayEventReceiver.registerReceiver(this);
    }
}
