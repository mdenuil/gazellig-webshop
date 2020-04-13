package com.kantilever.pcsbestellen.domain.bestelling.services;

import com.kantilever.pcsbestellen.domain.artikel.amqp.jackson.VoorraadVerhoogdEvent;
import com.kantilever.pcsbestellen.domain.bestelling.BestellingRepository;
import com.kantilever.pcsbestellen.domain.bestelling.amqp.events.BestellingEvent;
import com.kantilever.pcsbestellen.domain.bestelling.models.BestelStatusType;
import com.kantilever.pcsbestellen.domain.bestelling.models.Bestelling;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * BestellingEventService opens up all operations that can be done to a Bestelling. EventReceivers use this to transform
 * the events to Bestellingen and transform any data needed.
 */
@Service
@Log4j2
public class BestellingService {
    private BestellingRepository bestellingRepository;
    private BestellingEventService bestellingEventService;

    @Autowired
    public BestellingService(BestellingRepository bestellingRepository,
                             BestellingEventService bestellingEventService) {
        this.bestellingRepository = bestellingRepository;
        this.bestellingEventService = bestellingEventService;
    }

    /**
     * Before BestellingEvent is saved, default {@link BestelStatusType} value is 'BESTELD'
     *
     * @param bestellingEvent incomming event
     */
    public Bestelling saveNewBestelling(BestellingEvent bestellingEvent, BestelStatusType bestelStatusType) {
        bestellingEvent.setStatus(bestelStatusType);

        var bestelling = bestellingRepository.save(Bestelling.from(bestellingEvent));
        bestellingEventService.sendBestellingToegevoegdEvent(bestelling);

        return bestelling;
    }

    public List<Bestelling> findAllBestellingen() {
        List<Bestelling> bestellingen = new ArrayList<>();

        bestellingRepository.findAll().forEach(bestellingen::add);
        return bestellingen;
    }

    /**
     * This method will look for all bestellingen where status is IN_AFWACHTING (Pending)
     * This method is necessary when the Event {@link VoorraadVerhoogdEvent} is received so it can check again
     * for each Bestelling wether it is ready or not
     *
     * @return a list of Bestellingen
     */
    public List<Bestelling> findAllBestellingenWithStatusInAfwachting() {
        List<Bestelling> bestellingen = new ArrayList<>();

        bestellingRepository.findAllByStatus(BestelStatusType.IN_AFWACHTING).forEach(bestellingen::add);
        return bestellingen;
    }
}
