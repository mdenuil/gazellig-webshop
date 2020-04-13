package com.kantilever.bffbestellingen.domain.artikel.amqp.jackson;

import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.gazellig.amqpservice.amqp.config.EventSubtypeConfig;
import com.kantilever.bffbestellingen.domain.artikel.amqp.events.voorraad.VoorraadEvent;
import com.kantilever.bffbestellingen.domain.artikel.amqp.events.voorraad.VoorraadVerhoogdEvent;
import com.kantilever.bffbestellingen.domain.artikel.amqp.events.voorraad.VoorraadVerlaagdEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * VoorraadVeranderdSubtypesConfig registers the VoorraadEvent subtypes with the ObjectMapper for automatic
 * deserialization based on topic.
 */
@Configuration
public class VoorraadVeranderdSubtypesConfig extends EventSubtypeConfig<VoorraadEvent> {
    @Value("${rabbitmq.topics.VoorraadVerhoogd}")
    private String topicVoorraadVerhoogd;

    @Value("${rabbitmq.topics.VoorraadVerlaagd}")
    private String topicVoorraadVerlaagd;

    public VoorraadVeranderdSubtypesConfig() {
        super(VoorraadEvent.class);
    }

    @Override
    public void bindEventsToObjectMapper() {
        eventSubtypeModule.registerSubtypes(
                new NamedType(VoorraadVerhoogdEvent.class, topicVoorraadVerhoogd),
                new NamedType(VoorraadVerlaagdEvent.class, topicVoorraadVerlaagd)
        );
        objectMapper.registerModule(eventSubtypeModule);
    }

    String getTopicVoorraadVerhoogd() {
        return topicVoorraadVerhoogd;
    }

    String getTopicVoorraadVerlaagd() {
        return topicVoorraadVerlaagd;
    }
}
