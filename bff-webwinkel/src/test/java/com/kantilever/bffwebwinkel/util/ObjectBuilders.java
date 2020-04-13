package com.kantilever.bffwebwinkel.util;

import com.kantilever.bffwebwinkel.domain.artikel.amqp.events.ArtikelEvent;
import com.kantilever.bffwebwinkel.domain.artikel.amqp.events.VoorraadEvent;
import com.kantilever.bffwebwinkel.domain.artikel.models.Artikel;
import com.kantilever.bffwebwinkel.domain.bestelling.amqp.events.BestellingEvent;
import com.kantilever.bffwebwinkel.domain.bestelling.dto.AdresDto;
import com.kantilever.bffwebwinkel.domain.bestelling.dto.BesteldArtikelDto;
import com.kantilever.bffwebwinkel.domain.bestelling.dto.BestellingDto;
import com.kantilever.bffwebwinkel.domain.bestelling.models.*;
import com.kantilever.bffwebwinkel.domain.klant.amqp.events.KlantEvent;
import com.kantilever.bffwebwinkel.domain.klant.models.EKlantSoort;
import com.kantilever.bffwebwinkel.domain.klant.models.Klant;
import com.kantilever.bffwebwinkel.domain.klant.models.KlantSoort;
import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;

public class ObjectBuilders {
    public static Clock clock = Clock.fixed(Instant.now(), ZoneId.of("Europe/Paris"));
    public static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

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
                                .build(),
                        BesteldArtikel.builder()
                                .aantal(10)
                                .artikelNummer(2)
                                .build()));
    }

    public static BestellingDto.BestellingDtoBuilder getDefaultBestellingDtoBuilder() {
        return BestellingDto.builder()
                .initialen("V")
                .email("email@example.com")
                .achternaam("Achternaam")
                .status(null)
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

    public static BesteldArtikel.BesteldArtikelBuilder getDefaultBesteldArtikel() {
        return BesteldArtikel.builder()
                .aantal(10)
                .artikelNummer(1);
    }

    public static Set<Artikel> getDefaultSetofThreeArtikelen() {
        var artikel1 = Artikel.builder()
                .artikelNummer(1)
                .aantal(10)
                .prijs(BigDecimal.valueOf(25352))
                .naam("Workframe")
                .beschrijving("Alaaf alaaf")
                .afbeeldingUrl("cv.gif")
                .leverbaarVanaf(null)
                .leverbaarTot(null)
                .leverancier("Gazelle")
                .leverancierCode("Gazellig")
                .categorieen(List.of("Fiets", "E-Bike", "Bikelife"))
                .build();

        var artikel2 = Artikel.builder()
                .artikelNummer(2)
                .aantal(10)
                .prijs(BigDecimal.valueOf(25352))
                .naam("Dit is ook geen freem Alaaf")
                .beschrijving("Maar hier komt wel frame")
                .afbeeldingUrl("cv.gif")
                .leverbaarVanaf(null)
                .leverbaarTot(null)
                .leverancier("Gazelle")
                .leverancierCode("Gazellig")
                .categorieen(List.of("Fiets", "E-Bike", "Bikelife"))
                .build();

        var artikel3 = Artikel.builder()
                .artikelNummer(3)
                .aantal(10)
                .prijs(BigDecimal.valueOf(25352))
                .naam("Hier komt geen freem in voor")
                .beschrijving("Alaaf alaaf")
                .afbeeldingUrl("cv.gif")
                .leverbaarVanaf(null)
                .leverbaarTot(null)
                .leverancier("Gazelle")
                .leverancierCode("Gazellig")
                .categorieen(List.of("Fiets", "E-Bike", "Bikelife"))
                .build();

        return Set.of(
                artikel1,
                artikel2,
                artikel3
        );
    }

    public static List<Artikel> getDefaultListofThreeArtikelen() {
        var artikel1 = Artikel.builder()
                .artikelNummer(1)
                .aantal(10)
                .prijs(BigDecimal.valueOf(25352))
                .naam("Workframe")
                .beschrijving("Alaaf alaaffre")
                .afbeeldingUrl("cv.gif")
                .leverbaarVanaf(null)
                .leverbaarTot(null)
                .leverancier("Gazelle")
                .leverancierCode("Gazellig")
                .categorieen(List.of("Fiets", "E-Bike", "Bikelife"))
                .build();

        var artikel2 = Artikel.builder()
                .artikelNummer(2)
                .aantal(10)
                .prijs(BigDecimal.valueOf(25352))
                .naam("Dit is ook geen freem Alaaf")
                .beschrijving("Maar hier komt wel frame")
                .afbeeldingUrl("cv.gif")
                .leverbaarVanaf(null)
                .leverbaarTot(null)
                .leverancier("Gazelle")
                .leverancierCode("Gazellig")
                .categorieen(List.of("Fiets", "E-Bike", "Bikelife"))
                .build();

        var artikel3 = Artikel.builder()
                .artikelNummer(3)
                .aantal(10)
                .prijs(BigDecimal.valueOf(25352))
                .naam("Hier komt geen freem in voor")
                .beschrijving("Alaaf alaaf")
                .afbeeldingUrl("cv.gif")
                .leverbaarVanaf(null)
                .leverbaarTot(null)
                .leverancier("Gazelle")
                .leverancierCode("Gazellig")
                .categorieen(List.of("Fiets", "E-Bike", "Bikelife"))
                .build();

        return List.of(
                artikel1,
                artikel2,
                artikel3
        );
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
