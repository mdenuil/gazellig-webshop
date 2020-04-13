package com.kantilever.bffwebwinkel.domain.artikel.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kantilever.bffwebwinkel.domain.artikel.amqp.events.ArtikelEvent;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.*;

/**
 * Artikel represents a item that can be ordered from the webshop.
 */
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
public class Artikel {
    public static final BigDecimal BTW_PERCENTAGE = BigDecimal.valueOf(1.21);

    @Id
    private int artikelNummer;
    private int aantal;
    private BigDecimal prijs;
    private String naam;
    private String beschrijving;
    private String afbeeldingUrl;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private LocalDateTime leverbaarVanaf;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private LocalDateTime leverbaarTot;
    private String leverancierCode;
    private String leverancier;
    @ElementCollection
    private List<String> categorieen;

    public static Artikel from(ArtikelEvent artikelEvent) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

        LocalDateTime leverbaarTot = null;
        if (artikelEvent.getLeverbaarTot() != null) {
            leverbaarTot = LocalDateTime.parse(artikelEvent.getLeverbaarTot(), formatter);
        }

        return Artikel.builder()
                .artikelNummer(artikelEvent.getArtikelNummer())
                .prijs(BigDecimal.valueOf(artikelEvent.getPrijs()).multiply(BTW_PERCENTAGE))
                .naam(artikelEvent.getNaam())
                .beschrijving(artikelEvent.getBeschrijving())
                .afbeeldingUrl(artikelEvent.getAfbeeldingUrl())
                .leverbaarVanaf(LocalDateTime.parse(artikelEvent.getLeverbaarVanaf(), formatter))
                .leverbaarTot(leverbaarTot)
                .leverancierCode(artikelEvent.getLeverancierCode())
                .leverancier(artikelEvent.getLeverancier())
                .categorieen(artikelEvent.getCategorieen())
                .build();
    }

    /**
     * Increase the Aantal count by set amount.
     *
     * @param amount value to increase Aantal by
     */
    public void verhoogAantal(int amount) {
        this.aantal = this.aantal + amount;
    }

    /**
     * Decrease the Aantal count by set amount.
     *
     * @param amount value to decrease Aantal by
     */
    public void verlaagAantal(int amount) {
        this.aantal = this.aantal - amount;
    }
}
