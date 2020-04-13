package com.kantilever.pcsbestellen.domain.bestelling.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kantilever.pcsbestellen.domain.bestelling.amqp.events.BestellingStatusEvent;
import com.kantilever.pcsbestellen.domain.bestelling.amqp.senders.BestellingBehandelbaarEventSender;
import com.kantilever.pcsbestellen.domain.bestelling.amqp.senders.BestellingInAfwachtingEventSender;
import com.kantilever.pcsbestellen.domain.bestelling.models.Bestelling;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class BestellingStatusEventService {

    private BestellingBehandelbaarEventSender behandelbaarSender;
    private BestellingInAfwachtingEventSender inAfwachtingSender;

    @Autowired
    public BestellingStatusEventService(BestellingBehandelbaarEventSender behandelbaarSender,
                                        BestellingInAfwachtingEventSender inAfwachtingSender) {
        this.behandelbaarSender = behandelbaarSender;
        this.inAfwachtingSender = inAfwachtingSender;
    }

    void sendBestellingBehandelbaarEvent(Bestelling bestelling) {
        try {
            behandelbaarSender.send(BestellingStatusEvent.from(bestelling));
        } catch (JsonProcessingException e) {
            log.error("Error during processing BestellingBehandelbaarStatusEvent", e);
        }
    }

    void sendBestellingInAfwachtingEvent(Bestelling bestelling) {
        try {
            inAfwachtingSender.send(BestellingStatusEvent.from(bestelling));
        } catch (JsonProcessingException e) {
            log.error("Error during processing BestellingInAfwachtingStatusEvent", e);
        }
    }
}
