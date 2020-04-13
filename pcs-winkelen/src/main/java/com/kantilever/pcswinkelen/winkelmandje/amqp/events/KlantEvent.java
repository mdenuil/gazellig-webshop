package com.kantilever.pcswinkelen.winkelmandje.amqp.events;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.gazellig.amqpservice.amqp.events.AuditableEvent;
import lombok.*;

/**
 * KlantToegevoegdEvent represents the payload of an incoming KlantToegevoegdEvent.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class KlantEvent extends AuditableEvent {

    private Long klantNummer;

    @Builder
    public KlantEvent(String topic, Long klantNummer) {
        super(topic);
        this.klantNummer = klantNummer;
    }
}
