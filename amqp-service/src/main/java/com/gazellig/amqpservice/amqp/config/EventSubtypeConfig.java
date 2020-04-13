package com.gazellig.amqpservice.amqp.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gazellig.amqpservice.amqp.events.AuditableEvent;
import com.gazellig.amqpservice.amqp.jackson.EventSubtypeModule;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

/**
 * EventSubtypeConfig provides the bases for configuring subtypes of {@link AuditableEvent}
 *
 * @param <T> subtype of {@link AuditableEvent} where all to register subtypes should be based off.
 */
@Configuration
public abstract class EventSubtypeConfig<T extends AuditableEvent> {
    @Autowired
    protected ObjectMapper objectMapper;

    protected EventSubtypeModule<T> eventSubtypeModule;

    public EventSubtypeConfig(Class<T> eventType) {
        this.eventSubtypeModule = new EventSubtypeModule<>(eventType);
    }

    public EventSubtypeModule<T> getEventSubtypeModule() {
        return eventSubtypeModule;
    }

    /**
     * Override this function to declare extensions of the AuditableEvent. This is required for the Jackson mapper to
     * know how to map Subtypes of the {@link com.gazellig.amqpservice.amqp.events.AuditableEvent}.
     * <p>
     * Registered Bindings are required for each EventType and any Topic that they might be received on. It is impossible
     * to bind multiple topics to the same class due to Jackson Limitations I don't want to figure out a workaround for
     * so every Event class has to have exactly one topic bound to it.
     * <p>
     * eventSubtypeModule = new EventSubtypeModule(KlantEvent.class);
     * <p>
     * eventSubtypeModule.registerSubtypes(
     *     new NamedType(KlantGeregistreerd.class, topicKlantGeregistreerd)
     * );
     * <p>
     * objectMapper.registerModule(eventSubtypeModule);
     */
    @PostConstruct
    protected abstract void bindEventsToObjectMapper();
}
