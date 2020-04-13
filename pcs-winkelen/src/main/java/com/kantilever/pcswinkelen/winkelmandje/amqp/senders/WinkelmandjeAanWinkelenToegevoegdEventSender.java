package com.kantilever.pcswinkelen.winkelmandje.amqp.senders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gazellig.amqpservice.amqp.audit.senders.AuditableEventSender;
import com.kantilever.pcswinkelen.winkelmandje.amqp.events.WinkelmandjeEvent;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Sender which sends WinkelmandjeAanWinkelenToegevoegdEvent that will be sent to bff-webwinkel so a klant's
 * winkelmandje is persistent
 */
@Component
@Log4j2
public class WinkelmandjeAanWinkelenToegevoegdEventSender extends AuditableEventSender<WinkelmandjeEvent> {

    @Value("${rabbitmq.topics.WinkelmandjeAanWinkelenToegevoegd}")
    private String topic;

    @Override
    public void send(WinkelmandjeEvent event) throws JsonProcessingException {
        event.setTopic(topic);
        super.send(event);
    }

    public String getTopic() {
        return topic;
    }
}
