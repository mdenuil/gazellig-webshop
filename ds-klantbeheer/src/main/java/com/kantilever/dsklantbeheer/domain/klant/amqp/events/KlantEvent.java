package com.kantilever.dsklantbeheer.domain.klant.amqp.events;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.gazellig.amqpservice.amqp.events.AuditableEvent;
import com.kantilever.dsklantbeheer.domain.klant.models.EKlantSoort;
import com.kantilever.dsklantbeheer.domain.klant.models.Klant;
import com.kantilever.dsklantbeheer.domain.klant.models.KlantSoort;
import java.math.BigDecimal;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.*;

/**
 * KlantEvent represents an event received from RabbitMQ with an a Klant payload.
 * <p>
 * KlantEvent extends {@link AuditableEvent} to ensure it contains the basic event body properties.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties(ignoreUnknown = true)
public class KlantEvent extends AuditableEvent {
    private Long klantNummer;
    private String email;
    private String wachtwoord;
    private String initialen;
    private String achternaam;
    private BigDecimal krediet;
    private Set<EKlantSoort> klantSoort;

    public static KlantEvent from(Klant klant, String topic) {
        Set<EKlantSoort> klantSoort = klant.getKlantSoort().stream()
                .map(KlantSoort::getNaam)
                .collect(Collectors.toSet());

        return KlantEvent.builder()
                .topic(topic)
                .klantNummer(klant.getKlantNummer())
                .email(klant.getEmail())
                .wachtwoord(klant.getWachtwoord())
                .achternaam(klant.getAchternaam())
                .initialen(klant.getInitialen())
                .klantSoort(klantSoort)
                .krediet(klant.getKrediet())
                .build();
    }

    @Builder
    public KlantEvent(String topic, // NOSONAR
                      Long klantNummer,
                      String email,
                      String wachtwoord,
                      String initialen,
                      String achternaam,
                      BigDecimal krediet,
                      Set<EKlantSoort> klantSoort) {
        super(topic);
        this.klantNummer = klantNummer;
        this.email = email;
        this.wachtwoord = wachtwoord;
        this.initialen = initialen;
        this.achternaam = achternaam;
        this.krediet = krediet;
        this.klantSoort = klantSoort;
    }
}
