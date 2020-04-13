package com.kantilever.bffbestellingen.domain.artikel.amqp.jackson;

import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.gazellig.amqpservice.amqp.config.EventSubtypeConfig;
import com.kantilever.bffbestellingen.domain.artikel.amqp.events.artikel.ArtikelEvent;
import com.kantilever.bffbestellingen.domain.artikel.amqp.events.artikel.ArtikelToegevoegdEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * ArtikelSubtypesConfig registers the ArtikelEvent subtypes with the ObjectMapper for automatic deserialization based
 * on topic.
 */
@Configuration
public class ArtikelSubtypesConfig extends EventSubtypeConfig<ArtikelEvent> {
    @Value("${rabbitmq.topics.ArtikelAanCatalogusToegevoegd}")
    private String topicArtikelAanCatalogusToegevoegd;


    public ArtikelSubtypesConfig() {
        super(ArtikelEvent.class);
    }

    @Override
    public void bindEventsToObjectMapper() {
        eventSubtypeModule.registerSubtypes(
                new NamedType(ArtikelToegevoegdEvent.class, topicArtikelAanCatalogusToegevoegd));
        objectMapper.registerModule(eventSubtypeModule);
    }

    String getTopicArtikelAanCatalogusToegevoegd() {
        return topicArtikelAanCatalogusToegevoegd;
    }
}
