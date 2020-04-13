package com.kantilever.pcswinkelen.winkelmandje.amqp.jackson;

import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.gazellig.amqpservice.amqp.config.EventSubtypeConfig;
import com.kantilever.pcswinkelen.winkelmandje.amqp.events.KlantEvent;
import com.kantilever.pcswinkelen.winkelmandje.amqp.events.KlantToegevoegdEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KlantSubtypesConfig extends EventSubtypeConfig<KlantEvent> {

    @Value("${rabbitmq.topics.KlantToegevoegd}")
    private String topicKlantToegevoegd;

    public KlantSubtypesConfig() {
        super(KlantEvent.class);
    }

    @Override
    public void bindEventsToObjectMapper() {
        eventSubtypeModule.registerSubtypes(
                new NamedType(KlantToegevoegdEvent.class, topicKlantToegevoegd));

        objectMapper.registerModule(eventSubtypeModule);
    }

    String getTopicKlantToegevoegd() {
        return topicKlantToegevoegd;
    }
}
