package com.kantilever.pcsbestellen.domain.artikel.amqp.jackson;

import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.gazellig.amqpservice.amqp.config.EventSubtypeConfig;
import com.kantilever.pcsbestellen.domain.artikel.amqp.events.ArtikelEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ArtikelSubtypesConfig extends EventSubtypeConfig {
    @Value("${rabbitmq.topics.ArtikelAanCatalogusToegevoegd}")
    private String topicArtikelAanCatalogusToegevoegd;

    @Override
    public void bindEventsToObjectMapper() {
        SimpleModule eventSubtypes = new SimpleModule().registerSubtypes(
                new NamedType(ArtikelEvent.class, topicArtikelAanCatalogusToegevoegd));
        objectMapper.registerModule(eventSubtypes);
    }

}
