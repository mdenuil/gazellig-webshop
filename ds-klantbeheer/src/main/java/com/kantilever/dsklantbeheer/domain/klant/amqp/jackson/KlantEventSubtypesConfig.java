package com.kantilever.dsklantbeheer.domain.klant.amqp.jackson;

import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.gazellig.amqpservice.amqp.config.EventSubtypeConfig;
import com.kantilever.dsklantbeheer.domain.klant.amqp.events.KlantEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * KlantEventSubtypesConfig binds KlantEvent subtypes to a topic in the ObjectMapper.
 * <p>
 * The identifier of a KlantEvent is its topic and more than KlantEvent can be bound to a topic. To circumvent this
 * we create subtypes of KlantEvent and bind those to a specific topic.
 */
@Configuration
class KlantEventSubtypesConfig extends EventSubtypeConfig<KlantEvent> {
    @Value("${rabbitmq.topics.KlantGeregistreerdEvent}")
    private String topicKlantGeregistreerd;

    public KlantEventSubtypesConfig() {
        super(KlantEvent.class);
    }


    @Override
    public void bindEventsToObjectMapper() {
        eventSubtypeModule.registerSubtypes(
                new NamedType(KlantGeregistreerd.class, topicKlantGeregistreerd)
        );

        objectMapper.registerModule(eventSubtypeModule);
    }

    String getTopicKlantGeregistreerd() {
        return topicKlantGeregistreerd;
    }
}
