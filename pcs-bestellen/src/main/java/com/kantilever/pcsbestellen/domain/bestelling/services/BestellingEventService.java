package com.kantilever.pcsbestellen.domain.bestelling.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kantilever.pcsbestellen.domain.bestelling.amqp.events.BestellingEvent;
import com.kantilever.pcsbestellen.domain.bestelling.amqp.senders.BestellingEventSender;
import com.kantilever.pcsbestellen.domain.bestelling.models.Bestelling;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * BestellingEventService contains functions for all Events the Bestelling domain might send about changes to its data.
 *
 * This class uses known senders to direct Events to the correct topic.
 */
@Service
@Log4j2
public class BestellingEventService {
    @Value("${rabbitmq.topics.BestellingAanBestellenToegevoegd}")
    private String topicBestellingAanBestellenToegevoegd;

    private BestellingEventSender bestellingEventSender;

    @Autowired
    public BestellingEventService(BestellingEventSender bestellingEventSender) {
        this.bestellingEventSender = bestellingEventSender;
    }

    void sendBestellingToegevoegdEvent(Bestelling bestelling) {
        try {
            bestellingEventSender.send(BestellingEvent.from(bestelling, topicBestellingAanBestellenToegevoegd));
        } catch (JsonProcessingException e) {
            log.error("Error during event processing", e);
        }
    }
}
