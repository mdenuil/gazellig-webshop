package com.kantilever.bffwebwinkel.domain.klant.models;

import com.kantilever.bffwebwinkel.domain.klant.amqp.events.KlantEvent;
import com.kantilever.bffwebwinkel.domain.klant.services.KlantSoortService;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Klant represents a Klant for the Webwinkel. It holds all account information unique for a
 * shop user.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "KLANT",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "EMAIL")
        })
public class Klant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "KLANT_ID")
    private Long klantNummer;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "WACHTWOORD")
    private String wachtwoord;

    @Column(name = "INITIALEN")
    private String initialen;

    @Column(name = "ACHTERNAAM")
    private String achternaam;

    @Column(name = "KREDIET")
    private BigDecimal krediet;

    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(name = "KLANT_ROLES",
            joinColumns = @JoinColumn(name = "KLANT_ID"),
            inverseJoinColumns = @JoinColumn(name = "KLANTSOORT_ID"))
    private Set<KlantSoort> klantSoort = new HashSet<>();

    public static Klant from(KlantEvent klantEvent, KlantSoortService klantSoortService) {
        return Klant.builder()
                .klantNummer(klantEvent.getKlantNummer())
                .email(klantEvent.getEmail())
                .wachtwoord(klantEvent.getWachtwoord())
                .initialen(klantEvent.getInitialen())
                .achternaam(klantEvent.getAchternaam())
                .krediet(klantEvent.getKrediet())
                .klantSoort(klantSoortService.getKlantSoortFromEvent(klantEvent))
                .build();
    }
}
