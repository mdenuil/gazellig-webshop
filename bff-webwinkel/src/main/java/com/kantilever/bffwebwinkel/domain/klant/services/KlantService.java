package com.kantilever.bffwebwinkel.domain.klant.services;

import com.kantilever.bffwebwinkel.domain.klant.models.Klant;
import com.kantilever.bffwebwinkel.domain.klant.repositories.KlantRepository;
import javassist.NotFoundException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * KlantService is responsible for all persistence operations on {@link Klant} objects.
 */
@Service
@Log4j2
public class KlantService {
    private KlantRepository klantRepository;

    @Autowired
    public KlantService(KlantRepository klantRepository) {
        this.klantRepository = klantRepository;
    }

    /**
     * Persist a {@link Klant} entity.
     *
     * @param klant Entity to persist
     * @return persisted entity
     */
    public Klant save(Klant klant) {
        return klantRepository.save(klant);
    }

    /**
     * Find a {@link Klant} by Id. If no Klant can e found a {@link NotFoundException} is thrown.
     *
     * @param id id of klant to find
     * @return Found klant
     * @throws NotFoundException thrown when Klant could not be found for id
     */
    public Klant findKlantById(Long id) throws NotFoundException {
        return klantRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("No Klant with found with id: %s", id)));
    }
}
