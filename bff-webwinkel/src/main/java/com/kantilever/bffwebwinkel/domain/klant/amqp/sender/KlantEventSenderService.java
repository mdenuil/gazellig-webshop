package com.kantilever.bffwebwinkel.domain.klant.amqp.sender;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kantilever.bffwebwinkel.domain.klant.amqp.events.KlantEvent;
import com.kantilever.bffwebwinkel.domain.klant.models.Klant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * KlantEventSenderService is responsible for building {@link KlantEvent} from {@link Klant} objects and sending it over
 * RabbitMQ with the correct Topic.
 */
@Service
public class KlantEventSenderService {

    @Value("${rabbitmq.topics.KlantGeregistreerdEvent}")
    public String topicKlantGeregistreerd;

    private KlantEventSender klantEventSender;

    @Autowired
    public KlantEventSenderService(KlantEventSender klantEventSender) {
        this.klantEventSender = klantEventSender;
    }

    public void sendKlantGeregistreerdEvent(Klant klant) throws JsonProcessingException {
        klantEventSender.send(KlantEvent.from(klant, topicKlantGeregistreerd));
    }
}

