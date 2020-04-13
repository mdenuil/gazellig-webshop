package com.kantilever.bffwebwinkel.domain.klant.amqp.jackson;

import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.gazellig.amqpservice.amqp.config.EventSubtypeConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * KlantEventSubtypesConfig binds KlantEvent subtypes to a topic in the ObjectMapper.
 * <p>
 * The identifier of a KlantEvent is its topic and more than KlantEvent can be bound to a topic. To circumvent this
 * we create subtypes of KlantEvent and bind those to a specific topic.
 */
@Configuration
public class KlantEventSubtypesConfig extends EventSubtypeConfig {
    @Value("${rabbitmq.topics.KlantToegevoegdEvent}")
    private String topicKlantToegevoegd;


    @Override
    public void bindEventsToObjectMapper() {
        SimpleModule eventSubtypes = new SimpleModule().registerSubtypes(
                new NamedType(KlantToegevoegdEvent.class, topicKlantToegevoegd)
        );
        objectMapper.registerModule(eventSubtypes);
    }
}
