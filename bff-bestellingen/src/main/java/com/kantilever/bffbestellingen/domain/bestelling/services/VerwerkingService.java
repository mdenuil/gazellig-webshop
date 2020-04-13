package com.kantilever.bffbestellingen.domain.bestelling.services;

import com.kantilever.bffbestellingen.domain.bestelling.amqp.senders.BestellingEventSenderService;
import com.kantilever.bffbestellingen.domain.bestelling.dto.ArtikelVerwerktDto;
import com.kantilever.bffbestellingen.domain.bestelling.dto.BestellingKlaarDto;
import com.kantilever.bffbestellingen.domain.bestelling.models.BestelStatusType;
import com.kantilever.bffbestellingen.domain.bestelling.models.Bestelling;
import java.util.Optional;
import com.kantilever.bffbestellingen.domain.bestelling.repositories.BestellingRepository;
import javassist.NotFoundException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * VerwerkingService holds the business logic for processing of a {@link Bestelling}. Operations on a bestelling get
 * persisted via {@link BestellingRepository} and updates are send over AMQP via {@link BestellingEventSenderService}
 */
@Service
@Log4j2
public class VerwerkingService {
    private BestellingRepository bestellingRepository;
    private BestellingEventSenderService bestellingEventSenderService;

    @Autowired
    public VerwerkingService(BestellingRepository bestellingRepository,
                             BestellingEventSenderService bestellingEventSenderService) {
        this.bestellingRepository = bestellingRepository;
        this.bestellingEventSenderService = bestellingEventSenderService;
    }

    /**
     * If an open bestelling is available return that, else return the next bestelling
     * with status BEHANDELBAAR. Status is based on {@link BestelStatusType}.
     *
     * @return Next behandelbaar bestelling
     * @throws NotFoundException if no bestelling is available.
     */
    public Bestelling getNextBestelling() throws NotFoundException {
        var bestellingOpenOptional = getOpenBestelling();

        if(bestellingOpenOptional.isPresent()) {
            return bestellingOpenOptional.get();
        }

        var bestelling = getFirstBehandelbaar();
        bestelling.startVerwerken();
        bestellingRepository.save(bestelling);
        return bestelling;
    }

    /**
     * Mark an Artikel as done in a Bestelling that is currently being processed
     *
     * @param artikelVerwerktDto artikel to mark as done.
     * @return Bestelling with object marked as done.
     * @throws NotFoundException If the Bestelling can't be found.
     */
    public Bestelling verwerkArtikel(ArtikelVerwerktDto artikelVerwerktDto) throws NotFoundException {
        var bestelling = getBestelling(artikelVerwerktDto.getBestelNummer());

        if(!bestelling.getStatus().equals(BestelStatusType.IN_BEHANDELING)) {
            throw new IllegalStateException("Bestelling not in process");
        }

        bestelling.verwerkArtikel(artikelVerwerktDto.getArtikelNummer());
        return bestellingRepository.save(bestelling);
    }

    /**
     * Set a Bestelling as done. This function checks that the Bestelling has no more
     * open Artikelen. If it does an IllegalStateException is thrown.
     *
     * @param bestellingKlaarDto Object containing the Bestelling to mark as done.
     * @return The Bestelling set as done.
     * @throws NotFoundException when no bestelling can be found for bestelNummer
     */
    public Bestelling setBestellingKlaar(BestellingKlaarDto bestellingKlaarDto) throws NotFoundException {
        var bestelling = getBestelling(bestellingKlaarDto.getBestelNummer());

        if(bestelling.isKlaar()) {
            bestellingEventSenderService.sendBestellingVerstuurd(bestellingKlaarDto.getBestelNummer());
            bestelling.setStatus(BestelStatusType.VERSTUURD);
            return bestellingRepository.save(bestelling);
        } else {
            throw new IllegalStateException("Bestelling is not done");
        }
    }

    private Bestelling getBestelling(long id) throws NotFoundException {
        return bestellingRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("No such bestelling"));
    }

    private Bestelling getFirstBehandelbaar() throws NotFoundException {
        return bestellingRepository
                .findFirstByStatusOrderByBehandelbaarSindsDesc(BestelStatusType.BEHANDELBAAR)
                .orElseThrow(() -> new NotFoundException("No new bestelling"));
    }

    private Optional<Bestelling> getOpenBestelling() {
        return bestellingRepository.findFirstByStatusOrderByBehandelbaarSindsDesc(BestelStatusType.IN_BEHANDELING);
    }
}
