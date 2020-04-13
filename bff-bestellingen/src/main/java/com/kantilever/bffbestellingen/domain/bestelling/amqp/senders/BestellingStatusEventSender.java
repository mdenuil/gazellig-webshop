package com.kantilever.bffbestellingen.domain.bestelling.amqp.senders;

import com.gazellig.amqpservice.amqp.audit.senders.AuditableEventSender;
import com.kantilever.bffbestellingen.domain.bestelling.amqp.events.bestellingstatus.BestellingStatusEvent;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class BestellingStatusEventSender extends AuditableEventSender<BestellingStatusEvent> {
}
