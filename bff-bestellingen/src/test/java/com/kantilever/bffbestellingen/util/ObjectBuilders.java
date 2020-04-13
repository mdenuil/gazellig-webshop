package com.kantilever.bffbestellingen.util;

import com.kantilever.bffbestellingen.domain.artikel.amqp.events.artikel.ArtikelEvent;
import com.kantilever.bffbestellingen.domain.artikel.amqp.events.voorraad.VoorraadEvent;
import com.kantilever.bffbestellingen.domain.artikel.models.Artikel;
import com.kantilever.bffbestellingen.domain.bestelling.amqp.events.bestelling.BestellingEvent;
import com.kantilever.bffbestellingen.domain.bestelling.amqp.events.bestellingstatus.BestellingStatusEvent;
import com.kantilever.bffbestellingen.domain.bestelling.dto.AdresDto;
import com.kantilever.bffbestellingen.domain.bestelling.dto.ArtikelVerwerktDto;
import com.kantilever.bffbestellingen.domain.bestelling.dto.BesteldArtikelDto;
import com.kantilever.bffbestellingen.domain.bestelling.models.*;
import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ObjectBuilders {
    public static Clock clock = Clock.fixed(Instant.now(), ZoneId.of("Europe/Paris"));
    public static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public static Bestelling.BestellingBuilder getDefaultBestellingBuilder() {
        return Bestelling.builder()
                .bestelNummer(1L)
                .klantNummer(2L)
                .klantGegevens(new KlantGegevens(
                        "V",
                        "Achternaam",
                        "email@example.com"
                ))
                .status(BestelStatusType.BEHANDELBAAR)
                .factuurAdres(Adres.builder()
                        .huisnummer("12A")
                        .postcode("2082FA")
                        .straatnaam("FactuurStraat")
                        .woonplaats("Factuurville")
                        .build())
                .afleverAdres(Adres.builder()
                        .huisnummer("12A")
                        .postcode("2082AF")
                        .straatnaam("Afleverstraat")
                        .woonplaats("Afleverville")
                        .build())
                .artikelen(List.of(
                        BesteldArtikel.builder()
                                .aantal(10)
                                .artikelNummer(1)
                                .isVerwerkt(false)
                                .build(),
                        BesteldArtikel.builder()
                                .aantal(10)
                                .artikelNummer(2)
                                .isVerwerkt(false)
                                .build()));
    }

    public static BestellingEvent.BestellingEventBuilder getDefaultBestellingEventBuilder() {
        return BestellingEvent.builder()
                .bestelNummer(1L)
                .klantNummer(2L)
                .achternaam("Achternaam")
                .email("email@example.com")
                .status(BestelStatusType.BEHANDELBAAR)
                .topic("bestel.event.topic")
                .initialen("V")
                .factuurAdres(AdresDto.builder()
                        .huisnummer("12A")
                        .postcode("2082FA")
                        .straatnaam("FactuurStraat")
                        .woonplaats("Factuurville")
                        .build())
                .afleverAdres(AdresDto.builder()
                        .huisnummer("12A")
                        .postcode("2082AF")
                        .straatnaam("Afleverstraat")
                        .woonplaats("Afleverville")
                        .build())
                .artikelen(List.of(
                        BesteldArtikelDto.builder()
                                .aantal(10)
                                .artikelNummer(1)
                                .build(),
                        BesteldArtikelDto.builder()
                                .aantal(10)
                                .artikelNummer(2)
                                .build()
                ));
    }

    public static ArtikelVerwerktDto.ArtikelVerwerktDtoBuilder getDefaultArtikelVerwerktDto() {
        return ArtikelVerwerktDto.builder().artikelNummer(1).bestelNummer(1);
    }

    public static BesteldArtikel.BesteldArtikelBuilder getDefaultBesteldArtikel() {
        return BesteldArtikel.builder()
                .aantal(10)
                .artikelNummer(1)
                .isVerwerkt(false);
    }

    public static BestellingStatusEvent.BestellingStatusEventBuilder getDefaultBestellingStatusBuilder() {
        return BestellingStatusEvent.builder()
                .bestelNummer(1L)
                .status(BestelStatusType.IN_AFWACHTING);
    }

    public static ArtikelEvent.ArtikelEventBuilder getDefaultArtikelEvent() {
        return ArtikelEvent.builder()
                .topic("artike.topic")
                .afbeeldingUrl("AfbeeldingUrl")
                .artikelNummer(1)
                .beschrijving("Beschrijving")
                .categorieen(List.of("cat1", "cat2"))
                .leverancier("Leverancier")
                .leverancierCode("Lev")
                .leverbaarTot(LocalDateTime.now(clock).format(dateTimeFormatter))
                .leverbaarVanaf(LocalDateTime.now(clock).format(dateTimeFormatter))
                .naam("Naam")
                .prijs(100L);
    }

    public static Artikel.ArtikelBuilder getDefaultArtikel() {
        return Artikel.builder()
                .afbeeldingUrl("AfbeeldingUrl")
                .artikelNummer(1)
                .beschrijving("Beschrijving")
                .categorieen(List.of("cat1", "cat2"))
                .leverancier("Leverancier")
                .leverancierCode("Lev")
                .leverbaarTot(LocalDateTime.now(clock))
                .leverbaarVanaf(LocalDateTime.now(clock))
                .naam("Naam")
                .prijs(BigDecimal.valueOf(100));
    }

    public static VoorraadEvent.VoorraadEventBuilder getDefaultVoorraadEvent() {
        return VoorraadEvent.builder()
                .aantal(10)
                .artikelNummer(1);
    }
}
