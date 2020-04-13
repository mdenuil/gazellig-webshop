package com.kantilever.bffbestellingen.domain.bestelling.amqp.jackson;

import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.gazellig.amqpservice.amqp.config.EventSubtypeConfig;
import com.kantilever.bffbestellingen.domain.bestelling.amqp.events.bestelling.BestellingAanBestellenToegevoegdEvent;
import com.kantilever.bffbestellingen.domain.bestelling.amqp.events.bestelling.BestellingEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * BestellingSubtypesConfig registers the BestellingEvent subtypes with the ObjectMapper for automatic
 * deserialization based on topic.
 */
@Configuration
public class BestellingSubtypesConfig extends EventSubtypeConfig<BestellingEvent> {
    @Value("${rabbitmq.topics.BestellingAanBestellenToegevoegd}")
    private String topicBestellingAanBestellenToegevoegd;

    public BestellingSubtypesConfig() {
        super(BestellingEvent.class);
    }

    @Override
    public void bindEventsToObjectMapper() {
        eventSubtypeModule.registerSubtypes(
                new NamedType(BestellingAanBestellenToegevoegdEvent.class, topicBestellingAanBestellenToegevoegd)
        );

        objectMapper.registerModule(eventSubtypeModule);
    }

    String getTopicBestellingAanBestellenToegevoegd() {
        return topicBestellingAanBestellenToegevoegd;
    }
}
