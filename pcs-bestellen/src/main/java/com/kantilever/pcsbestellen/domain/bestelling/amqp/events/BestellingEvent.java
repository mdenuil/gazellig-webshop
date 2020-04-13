package com.kantilever.pcsbestellen.domain.bestelling.amqp.events;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.gazellig.amqpservice.amqp.events.AuditableEvent;
import java.util.List;
import java.util.stream.Collectors;
import com.kantilever.pcsbestellen.domain.bestelling.dto.AdresDto;
import com.kantilever.pcsbestellen.domain.bestelling.dto.BesteldArtikelDto;
import com.kantilever.pcsbestellen.domain.bestelling.models.BestelStatusType;
import com.kantilever.pcsbestellen.domain.bestelling.models.BesteldArtikel;
import com.kantilever.pcsbestellen.domain.bestelling.models.Bestelling;
import lombok.*;

/**
 * BestellingEvent is the payload of an event that has to do with Bestellingen.
 * <p>
 * Extends {@link AuditableEvent} to ensure it conforms to default event format.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
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

    public static BestellingEvent from(Bestelling bestelling, String topic) {
        var afleverAdres = AdresDto.builder()
                .straatnaam(bestelling.getAfleverAdres().getStraatnaam())
                .huisnummer(bestelling.getAfleverAdres().getHuisnummer())
                .postcode(bestelling.getAfleverAdres().getPostcode())
                .woonplaats(bestelling.getAfleverAdres().getWoonplaats())
                .build();

        var factuurAdres = AdresDto.builder()
                .straatnaam(bestelling.getFactuurAdres().getStraatnaam())
                .huisnummer(bestelling.getFactuurAdres().getHuisnummer())
                .postcode(bestelling.getFactuurAdres().getPostcode())
                .woonplaats(bestelling.getFactuurAdres().getWoonplaats())
                .build();

        return BestellingEvent.builder()
                .topic(topic)
                .bestelNummer(bestelling.getBestelNummer())
                .klantNummer(bestelling.getKlantNummer())
                .initialen(bestelling.getKlantGegevens().getInitialen())
                .achternaam(bestelling.getKlantGegevens().getAchternaam())
                .email(bestelling.getKlantGegevens().getEmail())
                .status(bestelling.getStatus())
                .afleverAdres(afleverAdres)
                .factuurAdres(factuurAdres)
                .artikelen(BestellingEvent.getBestelArtikelDtoList(bestelling.getArtikelen()))
                .build();
    }

    private static List<BesteldArtikelDto> getBestelArtikelDtoList(List<BesteldArtikel> besteldArtikelList) {
        return besteldArtikelList.stream()
                .map(besteldArtikel -> BesteldArtikelDto.builder()
                        .aantal(besteldArtikel.getAantal())
                        .artikelNummer(besteldArtikel.getArtikelNummer())
                        .build())
                .collect(Collectors.toList());
    }

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


