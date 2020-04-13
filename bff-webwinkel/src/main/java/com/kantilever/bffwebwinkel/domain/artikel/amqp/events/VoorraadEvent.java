package com.kantilever.bffwebwinkel.domain.artikel.amqp.events;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gazellig.amqpservice.amqp.events.AuditableEvent;
import lombok.*;

/**
 * VoorraadEvent represents the payload of a Voorraad event.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class VoorraadEvent extends AuditableEvent {
    @JsonProperty("Artikelnummer")
    private int artikelNummer;
    @JsonProperty("Aantal")
    private int aantal;
    @JsonProperty("NieuweVoorraad")
    private int nieuweVoorraad;

    @Builder
    public VoorraadEvent(String topic, int artikelNummer, int aantal, int nieuweVoorraad) {
        super(topic);
        this.artikelNummer = artikelNummer;
        this.aantal = aantal;
        this.nieuweVoorraad = nieuweVoorraad;
    }
}


