package com.kantilever.pcsbestellen.domain.bestelling.services;

import com.gazellig.amqpservice.amqp.audit.replay.AuditReplayStartListener;
import com.kantilever.pcsbestellen.domain.bestelling.amqp.events.BestellingEvent;
import com.kantilever.pcsbestellen.domain.bestelling.dto.AdresDto;
import com.kantilever.pcsbestellen.domain.bestelling.dto.BesteldArtikelDto;
import com.kantilever.pcsbestellen.domain.bestelling.models.BestelStatusType;
import com.kantilever.pcsbestellen.domain.bestelling.models.Bestelling;
import com.kantilever.pcsbestellen.domain.bestelling.services.BestellingEventService;
import com.kantilever.pcsbestellen.domain.bestelling.services.BestellingProcessorService;
import com.kantilever.pcsbestellen.domain.bestelling.services.BestellingService;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class BestellingServiceTest {
    @MockBean
    private AuditReplayStartListener auditReplayStartListener;

    @MockBean
    private BestellingEventService bestellingEventService;

    @MockBean
    private BestellingProcessorService bestellingProcessorService;

    private BestellingEvent bestellingEvent;

    @Autowired
    private BestellingService bestellingService;

    @BeforeEach
    void init() {
        bestellingEvent = BestellingEvent.builder()
                        .topic("")
                        .bestelNummer(1L)
                        .initialen("")
                        .achternaam("")
                        .email("")
                        .afleverAdres(new AdresDto())
                        .factuurAdres(new AdresDto())
                        .artikelen(List.of(new BesteldArtikelDto(1, 1)))
                        .build();
    }

    @Test
    @DisplayName("Een bestelling kan opgeslagen worden via de bestellingService")
    void canSaveBestellingViaBestellingService() {
        var bestelling = bestellingService.saveNewBestelling(bestellingEvent, BestelStatusType.IN_AFWACHTING);

        assertThat(bestelling.getStatus()).isEqualTo(BestelStatusType.IN_AFWACHTING);
    }

    @Test
    @DisplayName("Alle bestellingen met de status 'IN_AFWACHTING' worden als lijst teruggegeven")
    void allBestellingenWithStatusInAfwachtingAreReturnedAsList() {
        bestellingService.saveNewBestelling(bestellingEvent, BestelStatusType.IN_AFWACHTING);

        List<Bestelling> bestellingen = bestellingService.findAllBestellingenWithStatusInAfwachting();

        assertThat(bestellingen.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("Kan via de bestellingService een lijst teruggeven met daarin alle bestellingen")
    void allBestelingenAreReturnedAsList() {
        bestellingService.saveNewBestelling(bestellingEvent, BestelStatusType.IN_AFWACHTING);

        bestellingEvent.setBestelNummer(2L);
        bestellingService.saveNewBestelling(bestellingEvent, BestelStatusType.BETAALD);

        List<Bestelling> bestellingen = bestellingService.findAllBestellingen();

        assertThat(bestellingen.size()).isEqualTo(2);
    }
}