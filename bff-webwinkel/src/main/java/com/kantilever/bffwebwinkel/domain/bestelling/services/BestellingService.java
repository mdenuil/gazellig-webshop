package com.kantilever.bffwebwinkel.domain.bestelling.services;

import com.kantilever.bffwebwinkel.domain.bestelling.amqp.events.BestellingEvent;
import com.kantilever.bffwebwinkel.domain.bestelling.models.Bestelling;
import com.kantilever.bffwebwinkel.domain.bestelling.repositories.BestellingRepository;
import com.kantilever.bffwebwinkel.domain.klant.services.KlantService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * BestellingService is responsible for all persistence operations on {@link Bestelling} objects.
 */
@Service
public class BestellingService {

    private BestellingRepository bestellingRepository;

    @Autowired
    public BestellingService(BestellingRepository bestellingRepository) {
        this.bestellingRepository = bestellingRepository;
    }

    /**
     * Persist a {@link Bestelling} based on a {@link BestellingEvent}.
     *
     * @param bestellingEvent event containing Bestelling payload
     * @return persisted Bestelling
     */
    public Bestelling saveBestelling(BestellingEvent bestellingEvent) {
        return bestellingRepository.save(Bestelling.from(bestellingEvent));
    }

    /**
     * Retreive all {@link Bestelling} entities based on their klantId value.
     *
     * @param klantId id of the {@link com.kantilever.bffwebwinkel.domain.klant.models.Klant} to get the Bestelling for.
     * @return List of Bestellingen. Can be empty
     */
    public List<Bestelling> findAllBestellingenForKlant(Long klantId) {
        return bestellingRepository.findAllByKlantNummer(klantId);
    }
}
