package com.kantilever.pcsbestellen.domain.bestelling.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gazellig.amqpservice.amqp.audit.replay.AuditReplayStartListener;
import com.kantilever.pcsbestellen.domain.artikel.Artikel;
import com.kantilever.pcsbestellen.domain.artikel.ArtikelService;
import com.kantilever.pcsbestellen.domain.bestelling.amqp.events.BestellingEvent;
import com.kantilever.pcsbestellen.domain.bestelling.dto.AdresDto;
import com.kantilever.pcsbestellen.domain.bestelling.dto.BesteldArtikelDto;
import com.kantilever.pcsbestellen.domain.bestelling.models.BestelStatusType;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@SpringBootTest
@Transactional
class BestellingProcessorServiceTest {

    private BestellingEvent bestellingEvent;
    private Artikel artikel1, artikel2;

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
    private VoorraadMagazijnService voorraadMagazijnService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BestellingProcessorService bestellingProcessorService;

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
                .artikelen(List.of(new BesteldArtikelDto(1, 1),
                                   new BesteldArtikelDto(12, 9)))
                .build();

        artikel1 = Artikel.builder()
                .artikelNummer(1)
                .aantal(4)
                .prijs(BigDecimal.TEN)
                .naam("Opblaaskonijn")
                .beschrijving("Doet het goed op feestjes")
                .afbeeldingUrl("a.gif")
                .leverbaarVanaf(LocalDateTime.now())
                .leverbaarTot(LocalDateTime.now())
                .leverancierCode("FEYE")
                .leverancier("NOOIT")
                .categorieen(List.of("A", "B"))
                .build();

        artikel2 = Artikel.builder()
                .artikelNummer(9)
                .aantal(14)
                .prijs(BigDecimal.TEN)
                .naam("Opblaaskonijn")
                .beschrijving("Doet het goed op feestjes")
                .afbeeldingUrl("a.gif")
                .leverbaarVanaf(LocalDateTime.now())
                .leverbaarTot(LocalDateTime.now())
                .leverancierCode("FEYE")
                .leverancier("NOOIT")
                .categorieen(List.of("A", "B"))
                .build();
    }

    @Test
    @DisplayName("Kan een nieuwe BestellingEvent afhandelen")
    void canHandleNewBestellingEvent() {
        artikel1.decreaseAantal(4);

        when(artikelService.getArtikel(1))
                .thenReturn(Optional.of(artikel1));

        when(artikelService.getArtikel(9))
                .thenReturn(Optional.of(artikel2));

        bestellingProcessorService.handleBestellingEvent(bestellingEvent);

        verify(bestellingService, times(1))
                .saveNewBestelling(bestellingEvent, BestelStatusType.IN_AFWACHTING);
    }

    @Test
    @DisplayName("isBehandelbaar() geeft true terug als alle artikelen op voorraad zijn")
    void isBehandelbaarReturnsTrueWhenAllArtikelenAreAvailable() {
        when(artikelService.getArtikel(1))
                .thenReturn(Optional.of(artikel1));

        when(artikelService.getArtikel(9))
                .thenReturn(Optional.of(artikel2));

        boolean isBehandelbaar = bestellingProcessorService.isBehandelbaar(bestellingEvent.getArtikelen());

        assertThat(isBehandelbaar).isTrue();
    }

    @Test
    @DisplayName("isBehandelbaar() geeft false terug als een of meerdere artikelen niet op voorraad zijn")
    void isBehandelbaarReturnsFalseWhenOneOrMoreArtikelenAreNotAvailable() {
        bestellingEvent.setArtikelen(List.of(new BesteldArtikelDto(1, 1),
                new BesteldArtikelDto(15, 9)));

        when(artikelService.getArtikel(1))
                .thenReturn(Optional.of(artikel1));

        when(artikelService.getArtikel(9))
                .thenReturn(Optional.of(artikel2));

        boolean isBehandelbaar = bestellingProcessorService.isBehandelbaar(bestellingEvent.getArtikelen());

        assertThat(isBehandelbaar).isFalse();
    }

    @Test
    @DisplayName("isBehandelbaar() geeft false terug als een of meerdere artikelen niet bestaan (Empty)")
    void isBehandelbaarReturnsFalseWhenOneOrMoreArtikelenDontExist() {
        when(artikelService.getArtikel(1))
                .thenReturn(Optional.of(artikel1));

        when(artikelService.getArtikel(9))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> {
            bestellingProcessorService.isBehandelbaar(bestellingEvent.getArtikelen());
        })
        .isInstanceOf(NoSuchElementException.class)
        .hasMessage("No value present");
    }
}