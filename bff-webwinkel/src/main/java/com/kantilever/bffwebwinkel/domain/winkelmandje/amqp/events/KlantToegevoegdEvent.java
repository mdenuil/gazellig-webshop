package com.kantilever.bffwebwinkel.domain.winkelmandje.amqp.events;

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
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class KlantToegevoegdEvent extends AuditableEvent {

    private Long klantNummer;

}
