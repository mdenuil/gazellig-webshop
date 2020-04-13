package com.kantilever.bffwebwinkel.domain.artikel.services;

import com.google.common.collect.Lists;
import com.kantilever.bffwebwinkel.domain.artikel.amqp.events.ArtikelEvent;
import com.kantilever.bffwebwinkel.domain.artikel.amqp.events.VoorraadEvent;
import java.util.*;
import com.kantilever.bffwebwinkel.domain.artikel.models.Artikel;
import com.kantilever.bffwebwinkel.domain.artikel.repositories.ArtikelRepository;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * ArtikelService is responsible for all persistence operations on {@link Artikel} objects.
 */
@Service
public class ArtikelService {
    private ArtikelRepository artikelRepository;

    @Autowired
    public ArtikelService(ArtikelRepository artikelRepository) {
        this.artikelRepository = artikelRepository;
    }

    public List<Artikel> getAllArtikelen() {
        return Lists.newArrayList(this.artikelRepository.findAll());
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
     * This method will search in the {@link ArtikelRepository} for all Artikelen containing
     * the incoming parameter 'name' on the fields naam and beschrijving
     *
     * @param key as String where Upper/Lowercase is ignored
     * @return a filtered List of {@link Artikel}
     */
    public Set<Artikel> getArtikelenFilteredByNameAndDescription(String key) {
        Set<Artikel> artikelen = new LinkedHashSet<>();

        artikelen.addAll(artikelRepository.findAllByNaamContainingIgnoreCase(key));
        artikelen.addAll(artikelRepository.findAllByBeschrijvingContainingIgnoreCase(key));

        return artikelen;
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
