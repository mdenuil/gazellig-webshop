package com.kantilever.bffwebwinkel.domain.bestelling.amqp.jackson;

import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.gazellig.amqpservice.amqp.config.EventSubtypeConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * BestellingSubtypesConfig binds BestellingEvent subtypes to a topic in the ObjectMapper.
 * <p>
 * The identifier of a BestellingEvent is its topic and more than BestellingEvent can be bound to a topic. To circumvent this
 * we create subtypes of BestellingEvent and bind those to a specific topic.
 */
@Configuration
public class BestellingSubtypesConfig extends EventSubtypeConfig {
    @Value("${rabbitmq.topics.BestellingGeplaatstEvent}")
    private String topicBestellingGeplaatst;

    @Value("${rabbitmq.topics.BestellingAanBestellenToegevoegd}")
    private String topicBestellingAanBestellenToegevoegd;

    @Override
    public void bindEventsToObjectMapper() {
        SimpleModule eventSubtypes = new SimpleModule().registerSubtypes(
                new NamedType(BestellingGeplaatsEvent.class, topicBestellingGeplaatst),
                new NamedType(BestellingAanBestellenToegevoegdEvent.class, topicBestellingAanBestellenToegevoegd)
        );
        objectMapper.registerModule(eventSubtypes);
    }
}
