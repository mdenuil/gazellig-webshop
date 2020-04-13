package com.kantilever.bffwebwinkel.domain.klant.services;

import com.kantilever.bffwebwinkel.domain.klant.amqp.events.KlantEvent;
import com.kantilever.bffwebwinkel.domain.klant.models.EKlantSoort;
import com.kantilever.bffwebwinkel.domain.klant.models.KlantSoort;
import com.kantilever.bffwebwinkel.domain.klant.repositories.KlantSoortRepository;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * KlantSoortService is responsible for all persistence operations on {@link KlantSoort} objects.
 *
 * This class seeds the known Klantsoorten at startup of the service. The seeded Klantsoorten are
 * based on the enum types in {@link EKlantSoort}
 */
@Service
@Log4j2
public class KlantSoortService {
    private KlantSoortRepository klantSoortRepository;

    @Autowired
    public KlantSoortService(KlantSoortRepository klantSoortRepository) {
        this.klantSoortRepository = klantSoortRepository;
    }

    public Set<KlantSoort> getKlantSoortFromEvent(KlantEvent klantEvent) {
        return klantEvent.getKlantSoort().stream()
                .map(klantSoortRepository::findByNaam)
                .map(optionalKlantSoort -> optionalKlantSoort.orElseThrow(IllegalStateException::new))
                .collect(Collectors.toSet());
    }

    @PostConstruct
    private void seedKlantSoorten() {
        log.info("Seeding Klantsoorten");

        EKlantSoort[] klantSoorten = EKlantSoort.values();

        Arrays.stream(klantSoorten).forEach(eKlantSoort -> {
            if(klantSoortRepository.findByNaam(eKlantSoort).isEmpty()) {
                KlantSoort klantSoort = KlantSoort.builder().naam(eKlantSoort).build();
                klantSoortRepository.save(klantSoort);
            } else {
                log.info("Klantsoorten already present");
            }
        });
    }
}

