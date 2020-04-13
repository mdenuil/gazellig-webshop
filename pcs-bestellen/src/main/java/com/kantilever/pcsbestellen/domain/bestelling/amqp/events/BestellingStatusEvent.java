package com.kantilever.pcsbestellen.domain.bestelling.amqp.events;

import com.gazellig.amqpservice.amqp.events.AuditableEvent;
import com.kantilever.pcsbestellen.domain.bestelling.models.BestelStatusType;
import com.kantilever.pcsbestellen.domain.bestelling.models.Bestelling;
import lombok.*;

/**
 * BestellingStatusEvent is the payload of an event that has to do with the status change from Bestellingen.
 * <p>
 * Extends {@link AuditableEvent} to ensure it conforms to default event format.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class BestellingStatusEvent extends AuditableEvent {

    private Long bestelNummer;
    private BestelStatusType bestelStatusType;

    @Builder
    public BestellingStatusEvent(String topic, long bestelNummer, BestelStatusType bestelStatusType) {
        super(topic);
        this.bestelNummer = bestelNummer;
        this.bestelStatusType = bestelStatusType;
    }

    public static BestellingStatusEvent from(Bestelling bestelling) {
        return BestellingStatusEvent.builder()
                .bestelNummer(bestelling.getBestelNummer())
                .bestelStatusType(bestelling.getStatus())
                .build();
    }
}
