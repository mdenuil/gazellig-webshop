package com.kantilever.pcsbestellen.domain.bestelling.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kantilever.pcsbestellen.domain.bestelling.amqp.events.BestellingEvent;
import com.kantilever.pcsbestellen.domain.bestelling.dto.BesteldArtikelDto;
import com.kantilever.pcsbestellen.domain.bestelling.models.Bestelling;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import static com.kantilever.pcsbestellen.domain.bestelling.models.BestelStatusType.BEHANDELBAAR;
import static com.kantilever.pcsbestellen.domain.bestelling.models.BestelStatusType.IN_AFWACHTING;

@Service
@Log4j2
public class VoorraadMagazijnService {

    @Value("${magazijnservice.url}")
    private String magazijnService;

    @Value("${magazijnservice.port}")
    private String port;

    @Value("${magazijnservice.endpointVerlaagVoorraad}")
    private String endpointVerlaagVoorraad;

    @Value("${magazijnservice.endpointVerhoogVoorraad}")
    private String endpointVerhoogVoorraad;

    private BestellingService bestellingService;
    private BestellingStatusEventService bestellingStatusEventService;

    private RestTemplate restTemplate;
    private ObjectMapper objectMapper;

    private String url;

    @Autowired
    public VoorraadMagazijnService(BestellingService bestellingService,
                                   BestellingStatusEventService bestellingStatusEventService,
                                   RestTemplate restTemplate,
                                   ObjectMapper objectMapper) {
        this.bestellingService = bestellingService;
        this.bestellingStatusEventService = bestellingStatusEventService;
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;

    }

    @PostConstruct
    private void buildUrl() {
        url = magazijnService + ":" + port + "/";
    }

    /**
     * Checks if the incoming Bestelling is BEHANDELBAAR or not via the MagazijnService.
     * If it has not enough in stock from one Artikel in the list, it will return a 4xx status code and
     * saves bestelling with status IN_AFWACHTING instead of BEHANDELBAAR
     *
     * @param event {@link BestellingEvent} which you want to save
     */
    public void saveAsBestelling(BestellingEvent event) {
        var besteldeArtikelen = event.getArtikelen();

        if (isVoorraadVerlaagd(besteldeArtikelen)) {
            var bestelling = bestellingService.saveNewBestelling(event, BEHANDELBAAR);
            bestellingStatusEventService.sendBestellingBehandelbaarEvent(bestelling);
        } else {
            log.info(String.format("Magazijn out of stock for %s", event));
            var bestelling = bestellingService.saveNewBestelling(event, IN_AFWACHTING);
            bestellingStatusEventService.sendBestellingInAfwachtingEvent(bestelling);
        }
    }

    boolean isVoorraadVerlaagd(List<BesteldArtikelDto> besteldeArtikelen) {
        List<BesteldArtikelDto> completedArtikelen = new ArrayList<>();

        for (BesteldArtikelDto besteldArtikel : besteldeArtikelen) {
            try {
                HttpEntity<String> entity = createHttpEntity(besteldArtikel);
                restTemplate.exchange(url + endpointVerlaagVoorraad, HttpMethod.POST, entity, String.class);

                log.info(String.format("Sending POST Request to MagazijnService for Artikel: %s",
                        besteldArtikel.getArtikelNummer()));
            } catch (HttpClientErrorException e) {
                log.error("Magazijnservice responded with error code", e);
                restoreVoorraad(completedArtikelen);

                return false;
            }

            completedArtikelen.add(besteldArtikel);
        }

        return true;
    }
    
    HttpEntity<String> createHttpEntity(BesteldArtikelDto besteldArtikelDto) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        String artikel = "";

        try {
            artikel = objectMapper.writeValueAsString(besteldArtikelDto);
        } catch (JsonProcessingException e) {
            log.error(String.format("Json could not be parsed to a string for Artikel: %s", besteldArtikelDto), e);
        }

        return new HttpEntity<>(artikel, httpHeaders);
    }

    private void restoreVoorraad(List<BesteldArtikelDto> artikelen) {
        log.info("Restoring Voorraad since one Artikel was not available");

        for (BesteldArtikelDto artikel : artikelen) {
            HttpEntity<String> entity = createHttpEntity(artikel);

            try {
                restTemplate.exchange(url + endpointVerhoogVoorraad, HttpMethod.POST,
                                          entity, String.class);
            } catch (HttpClientErrorException e) {
                log.error("Magazijnservice responded with error code", e);
            }
        }
    }
}
