package com.kantilever.dsklantbeheer.domain.klant.amqp.sender;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kantilever.dsklantbeheer.domain.klant.amqp.events.KlantEvent;
import com.kantilever.dsklantbeheer.domain.klant.models.Klant;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * KlantEventSenderService is responsible for building {@link KlantEvent} from {@link Klant} objects and sending it over
 * RabbitMQ with the correct Topic.
 */
@Service
@Log4j2
public class KlantEventSenderService {
    @Value("${rabbitmq.topics.KlantToegevoegdEvent}")
    private String topicKlantToegevoegd;

    private KlantEventSender klantEventSender;

    @Autowired
    public KlantEventSenderService(KlantEventSender klantEventSender) {
        this.klantEventSender = klantEventSender;
    }

    public void sendKlantToegevoegdEvent(Klant klant) {
        try {
            klantEventSender.send(KlantEvent.from(klant, topicKlantToegevoegd));
        } catch (JsonProcessingException e) {
            log.error("Error during sending KlantToegevoegdEvent", e);
        }
    }

    String getTopicKlantToegevoegd() {
        return topicKlantToegevoegd;
    }
}
