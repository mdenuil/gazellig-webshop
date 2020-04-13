package com.kantilever.pcswinkelen.winkelmandje;

import com.kantilever.pcswinkelen.winkelmandje.amqp.events.KlantEvent;
import com.kantilever.pcswinkelen.winkelmandje.amqp.events.WinkelmandjeEvent;
import com.kantilever.pcswinkelen.winkelmandje.models.Winkelmandje;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * This Service can save new Winkelmandjes from an {@link WinkelmandjeEvent}
 * and update an existing {@link Winkelmandje}
 * Communicates with WinkelmandjeRepository to persist Winkelmandjes
 *
 * This service will also send an Event to RabbitMQ when something is saved/updated
 */
@Service
@Log4j2
public class WinkelmandjeService {

    private WinkelmandjeRepository winkelmandjeRepository;
    private WinkelmandjeEventService winkelmandjeEventService;

    @Autowired
    public WinkelmandjeService(WinkelmandjeRepository winkelmandjeRepository,
                               WinkelmandjeEventService winkelmandjeEventService) {
        this.winkelmandjeRepository = winkelmandjeRepository;
        this.winkelmandjeEventService = winkelmandjeEventService;
    }

    public Winkelmandje saveNewWinkelmandje(KlantEvent klantToegevoegdEvent) {
        var winkelmandje = Winkelmandje.from(klantToegevoegdEvent);
        return winkelmandjeRepository.save(winkelmandje);
    }

    public void updateExistingWinkelmandje(WinkelmandjeEvent winkelmandjeEvent) {
        winkelmandjeRepository
                .findById(winkelmandjeEvent.getKlantNummer())
                .ifPresent(winkelmandje -> {
                    winkelmandje.setArtikelen(winkelmandjeEvent.getArtikelen());
                    var savedWinkelmandje = winkelmandjeRepository.save(winkelmandje);

                    if (savedWinkelmandje != null) {
                        winkelmandjeEventService.sendWinkelmandjeToegevoegdEvent(winkelmandjeEvent);
                    }
                });
    }
}
