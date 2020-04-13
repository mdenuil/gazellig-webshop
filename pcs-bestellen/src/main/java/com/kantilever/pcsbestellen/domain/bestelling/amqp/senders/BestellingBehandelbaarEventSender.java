package com.kantilever.pcsbestellen.domain.bestelling.amqp.senders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gazellig.amqpservice.amqp.audit.senders.AuditableEventSender;
import com.kantilever.pcsbestellen.domain.bestelling.amqp.events.BestellingStatusEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class BestellingBehandelbaarEventSender extends AuditableEventSender<BestellingStatusEvent> {

    @Value("${rabbitmq.topics.BestellingBehandelbaar}")
    private String topicBestellingStatusBehandelbaar;

    @Override
    public void send(BestellingStatusEvent event) throws JsonProcessingException {
        event.setTopic(topicBestellingStatusBehandelbaar);
        super.send(event);
    }
}
