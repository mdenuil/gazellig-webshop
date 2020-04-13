package com.kantilever.bffbestellingen.domain.bestelling.amqp.senders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kantilever.bffbestellingen.domain.bestelling.amqp.events.bestellingstatus.BestellingStatusEvent;
import com.kantilever.bffbestellingen.domain.bestelling.models.BestelStatusType;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * BestellingEventSenderService acts as a gateway between the domain and the AMQP part. All domain services use this
 * class to send events through, rather than calling senders themselves.
 *
 * This class keeps tracks of available senders and the possible topics to send an event over.
 */
@Log4j2
@Service
public class BestellingEventSenderService {
    @Value("${rabbitmq.topics.BestellingVerstuurd}")
    private String bestellingVerstuurdTopic;

    private BestellingStatusEventSender bestellingStatusEventSender;

    @Autowired
    public BestellingEventSenderService(BestellingStatusEventSender bestellingStatusEventSender) {
        this.bestellingStatusEventSender = bestellingStatusEventSender;
    }

    /**
     * Send a BestellingVerstuurdEvent over the EventBus for a specific Bestelling, resulting in the
     * {@link BestelStatusType} to be set to VERSTUURD
     *
     * @param bestellingNummer number of the Bestelling to set status Verstuurd
     */
    public void sendBestellingVerstuurd(long bestellingNummer) {
        var event = BestellingStatusEvent.builder()
                .bestelNummer(bestellingNummer)
                .topic(bestellingVerstuurdTopic)
                .status(BestelStatusType.VERSTUURD)
                .build();

        try {
            bestellingStatusEventSender.send(event);
        } catch(JsonProcessingException e) {
            log.error("Error during sending BestellingVerstuurdEvent", e);
        }
    }

    String getBestellingVerstuurdTopic() {
        return bestellingVerstuurdTopic;
    }
}
