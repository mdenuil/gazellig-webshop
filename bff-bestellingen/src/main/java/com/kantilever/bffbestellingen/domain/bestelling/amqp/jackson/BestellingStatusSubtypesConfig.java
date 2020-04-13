package com.kantilever.bffbestellingen.domain.bestelling.amqp.jackson;

import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.gazellig.amqpservice.amqp.config.EventSubtypeConfig;
import com.kantilever.bffbestellingen.domain.bestelling.amqp.events.bestellingstatus.BestellingStatusBehandelbaarGezetEvent;
import com.kantilever.bffbestellingen.domain.bestelling.amqp.events.bestellingstatus.BestellingStatusEvent;
import com.kantilever.bffbestellingen.domain.bestelling.amqp.events.bestellingstatus.BestellingStatusVerstuurdGezetEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * BestellingStatusSubtypesConfig registers the BestellingStatusEvent subtypes with the ObjectMapper for automatic
 * deserialization based on topic.
 */
@Configuration
public class BestellingStatusSubtypesConfig extends EventSubtypeConfig<BestellingStatusEvent> {
    @Value("${rabbitmq.topics.BestellingStatusBehandelbaar}")
    private String topicBestellingStatusBehandelbaarGezet;

    @Value("${rabbitmq.topics.BestellingStatusVerstuurd}")
    private String topicBestellingStatusVerstuurd;

    public BestellingStatusSubtypesConfig() {
        super(BestellingStatusEvent.class);
    }

    @Override
    public void bindEventsToObjectMapper() {
        eventSubtypeModule.registerSubtypes(
                new NamedType(BestellingStatusBehandelbaarGezetEvent.class, topicBestellingStatusBehandelbaarGezet),
                new NamedType(BestellingStatusVerstuurdGezetEvent.class, topicBestellingStatusVerstuurd)
        );
        objectMapper.registerModule(eventSubtypeModule);
    }

    String getTopicBestellingStatusBehandelbaarGezet() {
        return topicBestellingStatusBehandelbaarGezet;
    }

    String getTopicBestellingStatusVerstuurd() {
        return topicBestellingStatusVerstuurd;
    }
}
