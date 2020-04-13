package com.kantilever.pcsbestellen.domain.bestelling.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gazellig.amqpservice.amqp.audit.replay.AuditReplayStartListener;
import com.kantilever.pcsbestellen.domain.artikel.ArtikelService;
import com.kantilever.pcsbestellen.domain.bestelling.amqp.events.BestellingEvent;
import com.kantilever.pcsbestellen.domain.bestelling.amqp.senders.BestellingBehandelbaarEventSender;
import com.kantilever.pcsbestellen.domain.bestelling.amqp.senders.BestellingInAfwachtingEventSender;
import com.kantilever.pcsbestellen.domain.bestelling.dto.AdresDto;
import com.kantilever.pcsbestellen.domain.bestelling.dto.BesteldArtikelDto;
import com.kantilever.pcsbestellen.domain.bestelling.models.BestelStatusType;
import com.kantilever.pcsbestellen.domain.bestelling.models.Bestelling;
import java.net.http.HttpResponse;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@SpringBootTest
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class VoorraadMagazijnServiceTest {

    private BestellingEvent bestellingEvent;

    @Value("${magazijnservice.url}")
    private String host;

    @Value("${magazijnservice.port}")
    private String port;

    @Value("${magazijnservice.endpointVerlaagVoorraad}")
    private String endpointVerlaagVoorraad;

    @Value("${magazijnservice.endpointVerhoogVoorraad}")
    private String endpointVerhoogVoorraad;

    @MockBean
    private AuditReplayStartListener auditReplayStartListener;

    @MockBean
    private BestellingService bestellingService;

    @MockBean
    private ArtikelService artikelService;

    @MockBean
    private RestTemplate restTemplate;

    @MockBean
    private BestellingStatusEventService bestellingStatusEventService;

    @MockBean
    private BestellingProcessorService bestellingProcessorService;

    @MockBean
    private BestellingBehandelbaarEventSender bestellingBehandelbaarEventSender;

    @MockBean
    private BestellingInAfwachtingEventSender bestellingInAfwachtingEventSender;

    @Autowired
    private VoorraadMagazijnService voorraadMagazijnService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void init() {
        bestellingEvent = BestellingEvent.builder()
                .topic("topic")
                .bestelNummer(1L)
                .initialen("")
                .achternaam("")
                .email("")
                .afleverAdres(new AdresDto())
                .factuurAdres(new AdresDto())
                .artikelen(List.of(new BesteldArtikelDto(0, 1),
                                   new BesteldArtikelDto(0, 9)))
                .build();
    }

    @Test
    @DisplayName("Kan een HttpEntity maken van 'artikelnummer' en 'aantal'")
    void canCreateHttpEntityFromArtikelNummerAndAantal() throws JsonProcessingException {
        var besteldArtikel = new BesteldArtikelDto(3, 6);
        String jsonArtikel = objectMapper.writeValueAsString(besteldArtikel);

        HttpEntity<String> entity =
                voorraadMagazijnService.createHttpEntity(new BesteldArtikelDto(3, 6));

        assertThat(entity.hasBody()).isTrue();
        assertThat(entity.getBody()).isEqualTo(jsonArtikel);
    }

    @Test
    @DisplayName("Kan een VoorraadVerlaagd HTTP POST Request indienen bij de magazijn service")
    void canCreateVoorraadVerlaagdPostRequestToMagazijnService() throws JsonProcessingException {
        var magazijnUrl = host + ":" + port + "/" + endpointVerhoogVoorraad;
        var artikelen = bestellingEvent.getArtikelen();

        HttpEntity<String> httpEntity = voorraadMagazijnService.createHttpEntity(artikelen.get(0));

        when(restTemplate.exchange(magazijnUrl,
                HttpMethod.POST,
                httpEntity,
                String.class)).thenReturn(ResponseEntity.status(HttpStatus.OK).build());

        assertThat(voorraadMagazijnService.isVoorraadVerlaagd(artikelen)).isTrue();
    }

    @Test
    @DisplayName("Kan een BestellingEvent als Bestelling opslaan")
    void canSaveBestellingEventAsBestelling() {
        when(bestellingService.saveNewBestelling(bestellingEvent, BestelStatusType.IN_AFWACHTING))
                .thenReturn(new Bestelling());

        voorraadMagazijnService.saveAsBestelling(bestellingEvent);

        var inOrder = inOrder(bestellingStatusEventService, bestellingService);

        inOrder.verify(bestellingService, times(1))
                .saveNewBestelling(any(), any());
        inOrder.verify(bestellingStatusEventService, times(1))
                .sendBestellingBehandelbaarEvent(any());
    }
}