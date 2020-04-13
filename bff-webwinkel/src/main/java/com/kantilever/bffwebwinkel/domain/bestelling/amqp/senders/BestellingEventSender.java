package com.kantilever.bffwebwinkel.domain.bestelling.amqp.senders;

import com.gazellig.amqpservice.amqp.audit.senders.AuditableEventSender;
import com.kantilever.bffwebwinkel.domain.bestelling.amqp.events.BestellingEvent;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class BestellingEventSender extends AuditableEventSender<BestellingEvent> { }
