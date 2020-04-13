package com.kantilever.bffwebwinkel.domain.winkelmandje.amqp.jackson;

import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.gazellig.amqpservice.amqp.config.EventSubtypeConfig;
import com.kantilever.bffwebwinkel.domain.winkelmandje.amqp.events.WinkelmandjeEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WinkelmandjeSubtypesConfig extends EventSubtypeConfig {

    @Value("${rabbitmq.topics.WinkelmandjeAanWinkelenToegevoegd}")
    private String topicWinkelmandjeAangepast;

    @Override
    public void bindEventsToObjectMapper() {
        SimpleModule eventSubtypes = new SimpleModule().registerSubtypes(
                new NamedType(WinkelmandjeEvent.class, topicWinkelmandjeAangepast));
        objectMapper.registerModule(eventSubtypes);
    }
}
