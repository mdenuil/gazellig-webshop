package com.kantilever.pcswinkelen.winkelmandje.amqp.jackson;

import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.gazellig.amqpservice.amqp.config.EventSubtypeConfig;
import com.kantilever.pcswinkelen.winkelmandje.amqp.events.WinkelmandjeAangepastEvent;
import com.kantilever.pcswinkelen.winkelmandje.amqp.events.WinkelmandjeEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WinkelmandjeSubtypesConfig extends EventSubtypeConfig<WinkelmandjeEvent> {

    @Value("${rabbitmq.topics.WinkelmandjeAangepast}")
    private String topicWinkelmandjeAangepast;

    public WinkelmandjeSubtypesConfig() {
        super(WinkelmandjeEvent.class);
    }

    @Override
    public void bindEventsToObjectMapper() {
        eventSubtypeModule.registerSubtypes(
                new NamedType(WinkelmandjeAangepastEvent.class, topicWinkelmandjeAangepast)
        );

        objectMapper.registerModule(eventSubtypeModule);
    }

    public String getTopicWinkelmandjeAangepast() {
        return topicWinkelmandjeAangepast;
    }
}
