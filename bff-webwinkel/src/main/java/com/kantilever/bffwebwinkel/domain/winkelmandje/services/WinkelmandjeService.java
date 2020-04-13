package com.kantilever.bffwebwinkel.domain.winkelmandje.services;

import com.kantilever.bffwebwinkel.domain.winkelmandje.WinkelmandjeRepository;
import com.kantilever.bffwebwinkel.domain.winkelmandje.amqp.events.WinkelmandjeEvent;
import com.kantilever.bffwebwinkel.domain.winkelmandje.amqp.receivers.WinkelmandjeAanWinkelenToegevoegdEventReceiver;
import com.kantilever.bffwebwinkel.domain.winkelmandje.models.Winkelmandje;
import com.kantilever.bffwebwinkel.domain.winkelmandje.models.WinkelmandjeArtikel;
import java.util.stream.Collectors;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * This Service updates a {@link Winkelmandje} when a receiver receives a {@link WinkelmandjeEvent}
 * Communicates with WinkelmandjeRepository to persist Winkelmandjes
 */
@Service
@Log4j2
public class WinkelmandjeService {

    private WinkelmandjeRepository winkelmandjeRepository;

    @Autowired
    public WinkelmandjeService(WinkelmandjeRepository winkelmandjeRepository) {
        this.winkelmandjeRepository = winkelmandjeRepository;
    }

    /**
     * This method finds and returns a whole Winkelmandje based on klantnummer
     *
     * @param id as klantnummer to find Winkelmandje that belongs to one Klant
     * @return {@link Winkelmandje} so it can be returned as ResponseBody in GET request
     * @throws java.util.NoSuchElementException when id is not found
     */
    public Winkelmandje getWinkelmandjeById(Long id) {
        return winkelmandjeRepository.findById(id)
                .orElseThrow();
    }

    /**
     * Saves empty {@link Winkelmandje} coupled to a klant via klantnummer
     *
     * @param klantNummer as Long to save it directly to the database
     */
    public void saveNewWinkelmandje(Long klantNummer) {
        var winkelmandje = Winkelmandje.builder()
                .klantNummer(klantNummer)
                .artikelen(null)
                .build();

        winkelmandjeRepository.save(winkelmandje);
    }

    /**
     * Updates an existing {@link Winkelmandje} by first finding the current Winkelmandje (by klantnummer)
     * and then checks if the Optional-Winkelmandje exists so it can set the
     * new list of {@link WinkelmandjeArtikel}
     *
     * After that Winkelmandje will update all existing items (WinkelmandjeArtikelen) to the incoming
     * WinkelmandjeArtikelen from {@link WinkelmandjeEvent}
     *
     * @param event given from the {@link WinkelmandjeAanWinkelenToegevoegdEventReceiver}
     * @throws java.util.NoSuchElementException when id is not found
     */
    public void updateWinkelmandje(WinkelmandjeEvent event) {
        winkelmandjeRepository
                .findById(event.getKlantNummer())
                .ifPresent(winkelmandje -> {
                    var list = event.getArtikelen().stream().map(x ->
                                WinkelmandjeArtikel.builder()
                                    .artikelNummer(x.getArtikelNummer())
                                    .aantal(x.getAantal())
                                    .build()
                            ).collect(Collectors.toList());

                    winkelmandje.setArtikelen(list);
                    winkelmandjeRepository.save(winkelmandje);
                });
    }
}
