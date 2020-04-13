package com.kantilever.pcsbestellen.domain.bestelling.amqp.senders;

import com.gazellig.amqpservice.amqp.audit.senders.AuditableEventSender;
import com.kantilever.pcsbestellen.domain.bestelling.amqp.events.BestellingEvent;
import org.springframework.stereotype.Component;

/**
 * BestellingToegevoegdSender sends events about changes to Bestelling entities.
 * <p>
 * This class extends {@link AuditableEventSender} which enables the events to be read from the auditlog later on.
 */
@Component
public class BestellingEventSender extends AuditableEventSender<BestellingEvent> {
}
