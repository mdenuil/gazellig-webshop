package com.kantilever.bffbestellingen.domain.bestelling.amqp.events.bestelling;

import java.util.List;
import com.gazellig.amqpservice.amqp.events.AuditableEvent;
import com.kantilever.bffbestellingen.domain.bestelling.dto.AdresDto;
import com.kantilever.bffbestellingen.domain.bestelling.dto.BesteldArtikelDto;
import com.kantilever.bffbestellingen.domain.bestelling.models.BestelStatusType;
import lombok.*;

/**
 * BestellingEvent represents the payload of an Bestelling AMQP Message.
 *
 * This class extends {@link AuditableEvent} to provide required attributes for handling in the Auditlog.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class BestellingEvent extends AuditableEvent {
    private Long bestelNummer;
    private Long klantNummer;
    private String initialen;
    private String achternaam;
    private String email;
    private BestelStatusType status;
    private AdresDto afleverAdres;
    private AdresDto factuurAdres;

    private List<BesteldArtikelDto> artikelen;

    @Builder
    public BestellingEvent(String topic, // NOSONAR
                           Long bestelNummer,
                           Long klantNummer,
                           String initialen,
                           String achternaam,
                           String email,
                           BestelStatusType status,
                           AdresDto afleverAdres,
                           AdresDto factuurAdres,
                           List<BesteldArtikelDto> artikelen) {
        super(topic);
        this.bestelNummer = bestelNummer;
        this.klantNummer = klantNummer;
        this.initialen = initialen;
        this.achternaam = achternaam;
        this.status = status;
        this.afleverAdres = afleverAdres;
        this.factuurAdres = factuurAdres;
        this.email = email;
        this.artikelen = artikelen;
    }
}


