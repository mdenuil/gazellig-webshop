package com.kantilever.bffbestellingen.domain.bestelling.amqp.receivers;

import com.kantilever.bffbestellingen.domain.bestelling.amqp.events.bestelling.BestellingEvent;
import com.kantilever.bffbestellingen.domain.bestelling.amqp.events.bestellingstatus.BestellingStatusEvent;
import com.kantilever.bffbestellingen.domain.bestelling.models.BestelStatusType;
import com.kantilever.bffbestellingen.domain.bestelling.models.Bestelling;
import com.kantilever.bffbestellingen.domain.bestelling.services.BestellingService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * BestellingEventReceiverService acts as a gateway between BestellingEvents and the underlying domain services.
 *
 * All events use this class to deposit their events. This class decides how those events affect the domain.
 */
@Service
@Log4j2
public class BestellingEventReceiverService {
    private BestellingService bestellingService;

    @Autowired
    public BestellingEventReceiverService(BestellingService bestellingService) {
        this.bestellingService = bestellingService;
    }

    Bestelling handleBestellingToegevoegdEvent(BestellingEvent bestellingEvent) {
        return bestellingService.addNewBestelling(Bestelling.from(bestellingEvent));
    }

    void handleBestellingStatusInAfwachtingGezetEvent(BestellingStatusEvent event) {
        bestellingService.setBestellingStatus(event.getBestelNummer(), BestelStatusType.IN_AFWACHTING);
    }

    void handleBestellingStatusBehandelbaarGezetEvent(BestellingStatusEvent event) {
        bestellingService.setBestellingStatus(event.getBestelNummer(), BestelStatusType.BEHANDELBAAR);
        bestellingService.setBehandelbaarSindsNow(event.getBestelNummer());
    }

    void handleBestellingStatusInBehandelingGezetEvent(BestellingStatusEvent event) {
        bestellingService.setBestellingStatus(event.getBestelNummer(), BestelStatusType.IN_BEHANDELING);
    }

    void handleBestellingStatusVerstuurdGezetEvent(BestellingStatusEvent event) {
        bestellingService.setBestellingStatus(event.getBestelNummer(), BestelStatusType.VERSTUURD);
    }

    void handleBestellingStatusBetaaldGezetEvent(BestellingStatusEvent event) {
        bestellingService.setBestellingStatus(event.getBestelNummer(), BestelStatusType.BETAALD);
    }
}
