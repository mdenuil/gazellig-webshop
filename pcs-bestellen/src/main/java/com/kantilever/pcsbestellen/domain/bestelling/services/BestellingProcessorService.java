package com.kantilever.pcsbestellen.domain.bestelling.services;

import com.kantilever.pcsbestellen.domain.artikel.ArtikelService;
import com.kantilever.pcsbestellen.domain.bestelling.amqp.events.BestellingEvent;
import com.kantilever.pcsbestellen.domain.bestelling.dto.BesteldArtikelDto;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.kantilever.pcsbestellen.domain.bestelling.models.BestelStatusType.IN_AFWACHTING;

@Service
@Log4j2
public class BestellingProcessorService {
    private BestellingService bestellingService;
    private ArtikelService artikelService;
    private BestellingStatusEventService bestellingStatusEventService;
    private VoorraadMagazijnService voorraadMagazijnService;

    @Autowired
    public BestellingProcessorService(BestellingService bestellingService,
                                      ArtikelService artikelService,
                                      BestellingStatusEventService bestellingStatusEventService,
                                      VoorraadMagazijnService voorraadMagazijnService) {
        this.bestellingService = bestellingService;
        this.artikelService = artikelService;
        this.bestellingStatusEventService = bestellingStatusEventService;
        this.voorraadMagazijnService = voorraadMagazijnService;
    }

    /**
     * This method checks if the incomming Bestelling is either BEHANDELBAAR or IN_AFWACHTING and then saves
     * it to the database with the correct status.
     *
     * isBehandelbaar(List of ArtikelDto) checks if the Voorraad is sufficient so it can send
     * a POST request to Ds-Magazijn to lower the Voorraad
     *
     * saveAsBestelling checks if Magazijn has enough Artikelen in stock.
     *
     * @param event {@link BestellingEvent}
     */
    public void handleBestellingEvent(BestellingEvent event) {
        log.info(String.format("Handling incoming BestellingEvent: %s", event));
        var besteldeArtikelen = event.getArtikelen();

        if (isBehandelbaar(besteldeArtikelen)) {
            log.info("Saving Bestelling as 'BEHANDELBAAR'");

            voorraadMagazijnService.saveAsBestelling(event);
        } else {
            log.info("Saving Bestelling as 'IN_AFWACHTING'");

            var bestelling = bestellingService.saveNewBestelling(event, IN_AFWACHTING);
            bestellingStatusEventService.sendBestellingInAfwachtingEvent(bestelling);
        }
    }

    boolean isBehandelbaar(List<BesteldArtikelDto> besteldeArtikelen) {
        for (BesteldArtikelDto besteldArtikel : besteldeArtikelen) {
            var artikel = artikelService.getArtikel(besteldArtikel.getArtikelNummer()).orElseThrow();

            if (artikel.getAantal() < besteldArtikel.getAantal()) {
                return false;
            }
        }

        return true;
    }
}
