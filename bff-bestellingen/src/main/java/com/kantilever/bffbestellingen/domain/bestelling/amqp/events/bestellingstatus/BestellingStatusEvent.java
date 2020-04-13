package com.kantilever.bffbestellingen.domain.bestelling.amqp.events.bestellingstatus;

import com.gazellig.amqpservice.amqp.events.AuditableEvent;
import com.kantilever.bffbestellingen.domain.bestelling.models.BestelStatusType;
import lombok.*;

/**
 * BestellingStatusEvent represents the payload of an BestellingStatus AMQP Message.
 *
 * This class extends {@link AuditableEvent} to provide required attributes for handling in the Auditlog.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class BestellingStatusEvent extends AuditableEvent {
    private Long bestelNummer;
    private BestelStatusType status;

    @Builder
    public BestellingStatusEvent(String topic, Long bestelNummer, BestelStatusType status) {
        super(topic);
        this.bestelNummer = bestelNummer;
        this.status = status;
    }
}
