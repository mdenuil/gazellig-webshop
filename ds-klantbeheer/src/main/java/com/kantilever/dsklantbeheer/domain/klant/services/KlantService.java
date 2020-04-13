package com.kantilever.dsklantbeheer.domain.klant.services;

import java.math.BigDecimal;
import com.kantilever.dsklantbeheer.domain.klant.models.Klant;
import com.kantilever.dsklantbeheer.domain.klant.repositories.KlantRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * KlantService is responsible for all persistence operations on {@link Klant} objects.
 */
@Service
@Log4j2
public class KlantService {
    private static final Long DEFAULT_KREDIET = 500L;
    private KlantRepository klantRepository;

    @Autowired
    public KlantService(KlantRepository klantRepository) {
        this.klantRepository = klantRepository;
    }

    /**
     * Add a new klant to the Klant register. Sets the default Krediet to 500.
     *
     * @param klant Klant to persist in the database.
     * @return Klant created, includes id.
     */
    public Klant addNewKlant(Klant klant) {
        klant.setKrediet(BigDecimal.valueOf(DEFAULT_KREDIET));
        return klantRepository.save(klant);
    }
}
