package com.kantilever.pcswinkelen.winkelmandje.amqp.events;

import com.gazellig.amqpservice.amqp.events.AuditableEvent;
import com.kantilever.pcswinkelen.winkelmandje.models.BesteldArtikel;
import java.util.List;
import lombok.*;

/**
 * WinkelmandjeEvent represents the payload of an incoming WinkelmandjeEvent.
 * This can either be WinkelmandjeAangepastEvent or WinkelmandjeAangemaaktEvent
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class WinkelmandjeEvent extends AuditableEvent {

    private Long klantNummer;
    private List<BesteldArtikel> artikelen;

    @Builder
    public WinkelmandjeEvent(String topic, Long klantNummer, List<BesteldArtikel> artikelen) {
        super(topic);
        this.klantNummer = klantNummer;
        this.artikelen = artikelen;
    }
}
