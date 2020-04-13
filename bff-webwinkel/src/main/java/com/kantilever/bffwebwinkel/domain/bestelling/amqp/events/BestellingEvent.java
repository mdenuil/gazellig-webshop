package com.kantilever.bffwebwinkel.domain.bestelling.amqp.events;

import com.gazellig.amqpservice.amqp.events.AuditableEvent;
import com.kantilever.bffwebwinkel.domain.bestelling.dto.AdresDto;
import com.kantilever.bffwebwinkel.domain.bestelling.dto.BesteldArtikelDto;
import com.kantilever.bffwebwinkel.domain.bestelling.dto.BestellingDto;
import com.kantilever.bffwebwinkel.domain.bestelling.models.BestelStatusType;
import java.util.List;
import lombok.*;

/**
 * BestellingEvent represents an event received from RabbitMQ with a Bestelling payload.
 * <p>
 * BestellingEvent extends {@link AuditableEvent} to ensure it contains the basic event body properties.
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

    public static BestellingEvent from(BestellingDto bestellingDto, String topic) {
        return BestellingEvent.builder()
                .topic(topic)
                .initialen(bestellingDto.getInitialen())
                .achternaam(bestellingDto.getAchternaam())
                .email(bestellingDto.getEmail())
                .status(bestellingDto.getStatus())
                .afleverAdres(bestellingDto.getAfleverAdres())
                .factuurAdres(bestellingDto.getFactuurAdres())
                .artikelen(bestellingDto.getArtikelen())
                .build();
    }
}


