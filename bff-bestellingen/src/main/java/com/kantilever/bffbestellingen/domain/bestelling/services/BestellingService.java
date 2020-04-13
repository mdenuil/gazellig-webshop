package com.kantilever.bffbestellingen.domain.bestelling.services;

import com.kantilever.bffbestellingen.domain.bestelling.models.BestelStatusType;
import com.kantilever.bffbestellingen.domain.bestelling.models.Bestelling;
import com.kantilever.bffbestellingen.domain.bestelling.repositories.BestellingRepository;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * BestellingService handles all persistence operations on {@link Bestelling} objects.
 */
@Service
@Log4j2
public class BestellingService {
    private Clock clock;
    private BestellingRepository bestellingRepository;

    @Autowired
    public BestellingService(BestellingRepository bestellingRepository,
                             Clock clock) {
        this.bestellingRepository = bestellingRepository;
        this.clock = clock;
    }

    public List<Bestelling> findAllBestellingen() {
        return bestellingRepository.findAll();
    }

    /**
     * Persist a new Bestelling object. When status is Behandelbaar the date is set of when this
     * Bestelling arrived in this service
     *
     * @param bestelling Bestelling to persist.
     * @return persisted Bestelling.
     */
    public Bestelling addNewBestelling(Bestelling bestelling) {
        if (bestelling.getStatus().equals(BestelStatusType.BEHANDELBAAR)) {
            bestelling.setBehandelbaarSinds(LocalDateTime.now(clock));
        }

        return bestellingRepository.save(bestelling);
    }

    /**
     * Set the bestelling status on a {@link Bestelling}. If no bestelling can be found it handles
     * it silently.
     *
     * @param bestelNummer of the Bestelling to set the status for.
     * @param status       the status to set on the Bestelling
     */
    public void setBestellingStatus(long bestelNummer, BestelStatusType status) {
        bestellingRepository.findById(bestelNummer).ifPresentOrElse(bestelling -> {
            bestelling.setStatus(status);
            bestellingRepository.save(bestelling);
        }, () -> log.error(String
                .format("Tried to update Bestelling status to %s on unknown bestelling %s",
                        status.getName(),
                        bestelNummer)));
    }

    /**
     * Set the behandelbaarSinds date to current time on a {@link Bestelling}. If no bestelling can be found it handles
     * it silently.
     *
     * @param bestelNummer of the Bestelling to set behandelbaarSinds on.
     */
    public void setBehandelbaarSindsNow(long bestelNummer) {
        bestellingRepository.findById(bestelNummer).ifPresentOrElse(bestelling -> {
            bestelling.setBehandelbaarSinds(LocalDateTime.now(clock));
            bestellingRepository.save(bestelling);
        }, () -> log.error(String
                .format("Tried to update Bestelling behandelbaar time on unknown bestelling %s",
                        bestelNummer)));
    }
}
