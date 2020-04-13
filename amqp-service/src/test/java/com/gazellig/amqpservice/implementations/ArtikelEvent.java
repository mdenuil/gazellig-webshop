package com.gazellig.amqpservice.implementations;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.gazellig.amqpservice.amqp.events.AuditableEvent;
import lombok.*;

/**
 * ArtikelEvent represents the payload of an Artikel event.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ArtikelEvent extends AuditableEvent {
    @JsonProperty("Artikelnummer")
    private int artikelNummer;
    @JsonProperty("Naam")
    private String naam;
    @JsonProperty("Beschrijving")
    private String beschrijving;
    @JsonProperty("Prijs")
    private Long prijs;
    @JsonProperty("AfbeeldingUrl")
    private String afbeeldingUrl;
    @JsonProperty("LeverbaarVanaf")
    private String leverbaarVanaf;
    @JsonProperty("LeverbaarTot")
    private String leverbaarTot;
    @JsonProperty("Leveranciercode")
    private String leverancierCode;
    @JsonProperty("Leverancier")
    private String leverancier;
    @JsonProperty("Categorieen")
    private List<String> categorieen;

    @Builder
    public ArtikelEvent(String topic, // NOSONAR
                        int artikelNummer,
                        String naam,
                        String beschrijving,
                        Long prijs,
                        String afbeeldingUrl,
                        String leverbaarVanaf,
                        String leverbaarTot,
                        String leverancierCode,
                        String leverancier,
                        List<String> categorieen) {
        super(topic);
        this.artikelNummer = artikelNummer;
        this.naam = naam;
        this.beschrijving = beschrijving;
        this.prijs = prijs;
        this.afbeeldingUrl = afbeeldingUrl;
        this.leverbaarVanaf = leverbaarVanaf;
        this.leverbaarTot = leverbaarTot;
        this.leverancierCode = leverancierCode;
        this.leverancier = leverancier;
        this.categorieen = categorieen;
    }
}

