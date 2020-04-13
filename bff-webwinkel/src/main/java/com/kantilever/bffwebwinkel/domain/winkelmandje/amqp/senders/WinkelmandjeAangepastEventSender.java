package com.kantilever.bffwebwinkel.domain.winkelmandje.amqp.senders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gazellig.amqpservice.amqp.audit.senders.AuditableEventSender;
import com.kantilever.bffwebwinkel.domain.winkelmandje.amqp.events.WinkelmandjeEvent;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Sender which sends WinkelmandjeAangepastEvent to pcs-winkelen so that the pcs-winkelen service can
 * persist a customers Winkelmandje
 */
@Component
@Log4j2
public class WinkelmandjeAangepastEventSender extends AuditableEventSender<WinkelmandjeEvent> {

    @Value("${rabbitmq.topics.WinkelmandjeAangepast}")
    private String topic;

    @Override
    public void send(WinkelmandjeEvent event) throws JsonProcessingException {
        event.setTopic(topic);
        super.send(event);
    }
}
