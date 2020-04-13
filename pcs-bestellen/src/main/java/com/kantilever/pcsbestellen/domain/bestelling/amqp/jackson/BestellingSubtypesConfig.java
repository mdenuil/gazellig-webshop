package com.kantilever.pcsbestellen.domain.bestelling.amqp.jackson;

import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.gazellig.amqpservice.amqp.config.EventSubtypeConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BestellingSubtypesConfig extends EventSubtypeConfig {
    @Value("${rabbitmq.topics.BestellingGeplaatstEvent}")
    private String topicBestellingGeplaatstEvent;

    @Value("${rabbitmq.topics.BestellingAanBestellenToegevoegd}")
    private String topicBestellingAanBestellenToegevoegd;

    @Override
    public void bindEventsToObjectMapper() {
        SimpleModule eventSubtypes = new SimpleModule().registerSubtypes(
                new NamedType(BestellingGeplaatstEvent.class, topicBestellingGeplaatstEvent),
                new NamedType(BestellingAanBestellenToegevoegdEvent.class, topicBestellingAanBestellenToegevoegd)
        );
        objectMapper.registerModule(eventSubtypes);
    }
}
