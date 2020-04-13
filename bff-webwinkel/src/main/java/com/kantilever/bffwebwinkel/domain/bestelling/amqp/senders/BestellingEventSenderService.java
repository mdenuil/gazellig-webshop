package com.kantilever.bffwebwinkel.domain.bestelling.amqp.senders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kantilever.bffwebwinkel.domain.bestelling.amqp.events.BestellingEvent;
import com.kantilever.bffwebwinkel.domain.bestelling.dto.BestellingDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class BestellingEventSenderService {
    @Value("${rabbitmq.topics.BestellingGeplaatstEvent}")
    private String topicVoegBestellingToeAanBestellen;

    private BestellingEventSender bestellingEventSender;

    @Autowired
    public BestellingEventSenderService(BestellingEventSender bestellingEventSender) {
        this.bestellingEventSender = bestellingEventSender;
    }

    public void sendBestellingGeplaatstEvent(BestellingDto bestellingDto, Long klantId) throws JsonProcessingException {
        var bestellingGeplaatstEvent = BestellingEvent.from(bestellingDto, topicVoegBestellingToeAanBestellen);
        bestellingGeplaatstEvent.setKlantNummer(klantId);
        bestellingEventSender.send(bestellingGeplaatstEvent);
    }

    String getTopicVoegBestellingToeAanBestellen() {
        return topicVoegBestellingToeAanBestellen;
    }
}
