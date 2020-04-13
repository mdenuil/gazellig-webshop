package com.kantilever.bffbestellingen.domain.artikel.services;

import com.kantilever.bffbestellingen.domain.artikel.amqp.events.artikel.ArtikelEvent;
import com.kantilever.bffbestellingen.domain.artikel.amqp.events.voorraad.VoorraadEvent;
import com.kantilever.bffbestellingen.domain.artikel.models.Artikel;
import com.kantilever.bffbestellingen.domain.artikel.repositories.ArtikelRepository;
import java.util.List;
import javassist.NotFoundException;
import org.springframework.stereotype.Service;

/**
 * ArtikelService is responsible for all persistence operations on {@link Artikel} objects.
 */
@Service
public class ArtikelService {
    private ArtikelRepository artikelRepository;

    public ArtikelService(ArtikelRepository artikelRepository) {
        this.artikelRepository = artikelRepository;
    }

    /**
     * Get a list of all Artikel items persisted.
     *
     * @return List of all Artikel items
     */
    public List<Artikel> getAllArtikelen() {
        return this.artikelRepository.findAll();
    }

    /**
     * Find an Artikel by its artikelNummer.
     *
     * @param artikelNummer number of Artikel to find
     * @return Artikel found for artikelNummer
     * @throws NotFoundException when artikelNummer does not match any Artikel
     */
    public Artikel getArtikel(int artikelNummer) throws NotFoundException {
        return artikelRepository.findById(artikelNummer)
                .orElseThrow(() -> new NotFoundException("No Artikel for this id"));
    }

    /**
     * Persist a new Artikel in the database from an ArtikelEVent
     *
     * @param artikelEvent event to build Artikel from.
     * @return persisted Artikel
     */
    public Artikel addArtikel(ArtikelEvent artikelEvent) {
        var artikel = Artikel.from(artikelEvent);
        return artikelRepository.save(artikel);
    }

    /**
     * Increase Aantal on an Artikel. If artikelNummer can't be found it
     * is handled silently.
     *
     * @param voorraadEvent the event containing voorraad values.
     */
    public void verhoogAantal(VoorraadEvent voorraadEvent) {
        artikelRepository
                .findById(voorraadEvent.getArtikelNummer())
                .ifPresent(artikel -> {
                    artikel.verhoogAantal(voorraadEvent.getAantal());
                    artikelRepository.save(artikel);
                });
    }

    /**
     * Decrease Aantal on an Artikel. If artikelNummer can't be found it
     * is handled silently.
     *
     * @param voorraadEvent the event containing voorraad values.
     */
    public void verlaagAantal(VoorraadEvent voorraadEvent) {
        artikelRepository
                .findById(voorraadEvent.getArtikelNummer())
                .ifPresent(artikel -> {
                    artikel.verlaagAantal(voorraadEvent.getAantal());
                    artikelRepository.save(artikel);
                });
    }
}
