package com.kantilever.bffwebwinkel.domain.klant.amqp.sender;

import com.gazellig.amqpservice.amqp.audit.senders.AuditableEventSender;
import com.kantilever.bffwebwinkel.domain.klant.amqp.events.KlantEvent;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

/**
 * KlantEventSender enables sending KlantEvent objects to RabbitMQ.
 * <p>
 * This class extends {@link AuditableEventSender} to create a proper Event message that can later
 * be read from the Auditlog.
 * <p>
 * The topic that an event is send over is based on the topic already register in the KlantEvent body.
 */
@Log4j2
@Component
public class KlantEventSender extends AuditableEventSender<KlantEvent> { }
