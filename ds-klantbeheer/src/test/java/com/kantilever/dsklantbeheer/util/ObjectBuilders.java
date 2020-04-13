package com.kantilever.dsklantbeheer.util;

import java.math.BigDecimal;
import java.util.Set;
import com.kantilever.dsklantbeheer.domain.klant.amqp.events.KlantEvent;
import com.kantilever.dsklantbeheer.domain.klant.models.EKlantSoort;
import com.kantilever.dsklantbeheer.domain.klant.models.Klant;
import com.kantilever.dsklantbeheer.domain.klant.models.KlantSoort;

public class ObjectBuilders {

    public static Klant.KlantBuilder getDefaultKlant() {
        return Klant.builder()
                .klantNummer(1L)
                .klantSoort(Set.of(getDefaultKlantSoort().build()))
                .email("klant@example.com")
                .wachtwoord("$2a$10$nyAuXNIOR2VV5Q3zq8d/Zeta2AJogLa6jKH1vto8O5WB/qpusBI4K")
                .initialen("K")
                .krediet(BigDecimal.valueOf(500))
                .achternaam("Klant");

    }

    public static KlantSoort.KlantSoortBuilder getDefaultKlantSoort() {
        return KlantSoort.builder()
                .naam(EKlantSoort.PARTICULIER);
    }

    public static KlantEvent.KlantEventBuilder getDefaultKlantEvent() {
        return KlantEvent.builder()
                .klantNummer(1L)
                .klantSoort(Set.of(EKlantSoort.PARTICULIER))
                .email("klant@example.com")
                .wachtwoord("$2a$10$nyAuXNIOR2VV5Q3zq8d/Zeta2AJogLa6jKH1vto8O5WB/qpusBI4K")
                .initialen("K")
                .krediet(BigDecimal.valueOf(500))
                .achternaam("Klant");
    }
}
