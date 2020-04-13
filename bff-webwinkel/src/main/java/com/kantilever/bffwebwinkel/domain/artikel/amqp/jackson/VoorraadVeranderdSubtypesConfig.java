package com.kantilever.bffwebwinkel.domain.artikel.amqp.jackson;

import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.gazellig.amqpservice.amqp.config.EventSubtypeConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * VoorraadVeranderdSubtypesConfig registers the VoorraadEvent subtypes with the ObjectMapper for automatic
 * deserialization based on topic.
 */
@Configuration
public class VoorraadVeranderdSubtypesConfig extends EventSubtypeConfig {
    @Value("${rabbitmq.topics.VoorraadVerhoogd}")
    private String topicVoorraadVerhoogd;

    @Value("${rabbitmq.topics.VoorraadVerlaagd}")
    private String topicVoorraadVerlaagd;

    @Override
    public void bindEventsToObjectMapper() {
        SimpleModule eventSubtypes = new SimpleModule().registerSubtypes(
                new NamedType(VoorraadVerhoogdEvent.class, topicVoorraadVerhoogd),
                new NamedType(VoorraadVerlaagdEvent.class, topicVoorraadVerlaagd)
        );
        objectMapper.registerModule(eventSubtypes);
    }
}
