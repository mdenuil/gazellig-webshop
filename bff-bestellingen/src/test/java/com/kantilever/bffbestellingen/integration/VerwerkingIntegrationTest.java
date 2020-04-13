package com.kantilever.bffbestellingen.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gazellig.amqpservice.amqp.audit.replay.AuditReplayStartListener;
import com.kantilever.bffbestellingen.domain.bestelling.repositories.BestellingRepository;
import com.kantilever.bffbestellingen.domain.bestelling.controllers.VerwerkingController;
import com.kantilever.bffbestellingen.domain.bestelling.amqp.events.bestellingstatus.BestellingStatusEvent;
import com.kantilever.bffbestellingen.domain.bestelling.amqp.senders.BestellingStatusEventSender;
import com.kantilever.bffbestellingen.domain.bestelling.dto.ArtikelVerwerktDto;
import com.kantilever.bffbestellingen.domain.bestelling.dto.BestellingKlaarDto;
import com.kantilever.bffbestellingen.domain.bestelling.models.BestelStatusType;
import com.kantilever.bffbestellingen.domain.bestelling.models.Bestelling;
import com.kantilever.bffbestellingen.util.ObjectBuilders;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;


@SpringBootTest
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class VerwerkingIntegrationTest {
    @MockBean
    private AuditReplayStartListener auditReplayStartListener;

    @MockBean
    BestellingStatusEventSender bestellingStatusEventSender;

    @Autowired
    private VerwerkingController verwerkingController;

    @Autowired
    private BestellingRepository bestellingRepository;

    @Test
    @DisplayName("If no bestelling exists the function returns null")
    void withNoBestellingExists_getVolgendeBestelling_returnsNull() {
        // Arrange
        // Act
        // Assert
        assertThatThrownBy(() -> verwerkingController.getNextBehandelbaarBestelling())
                .isInstanceOf(ResponseStatusException.class);
    }

    @Test
    @DisplayName("A new bestelling is returned and set to IN_BEHANDELING if a BEHANDELBAAR exists")
    void withBestelingBehandelbaarExists_newBestellingIsReturned_andSetAsInBehandeling() {
        // Arrange
        Bestelling bestelling = ObjectBuilders.getDefaultBestellingBuilder()
                .status(BestelStatusType.BEHANDELBAAR)
                .bestelNummer(1L)
                .behandelbaarSinds(LocalDateTime.now())
                .build();
        bestellingRepository.save(bestelling);
        // Act
        var actual = verwerkingController.getNextBehandelbaarBestelling();
        // Assert
        assertThat(actual.getBestelNummer()).isEqualTo(1L);
        assertThat(actual.getStatus()).isEqualTo(BestelStatusType.IN_BEHANDELING);
    }

    @Test
    @DisplayName("If a bestelling already exists as being IN_BEHANDELING then that one is returned first")
    void whenInBehandelingBestellingExists_thatOneIsReturnedFirst() {
        // Arrange
        Bestelling bestellingInBehandeling = ObjectBuilders.getDefaultBestellingBuilder()
                .status(BestelStatusType.IN_BEHANDELING)
                .bestelNummer(1L)
                .behandelbaarSinds(LocalDateTime.now())
                .build();
        Bestelling bestellingBehandelbaar = ObjectBuilders.getDefaultBestellingBuilder()
                .status(BestelStatusType.BEHANDELBAAR)
                .bestelNummer(2L)
                .behandelbaarSinds(LocalDateTime.now())
                .build();
        bestellingRepository.save(bestellingInBehandeling);
        bestellingRepository.save(bestellingBehandelbaar);
        // Act
        var actual = verwerkingController.getNextBehandelbaarBestelling();
        // Assert
        assertThat(actual.getBestelNummer()).isEqualTo(1L);
        assertThat(actual.getStatus()).isEqualTo(BestelStatusType.IN_BEHANDELING);
    }

    @Test
    @DisplayName("If 2 BEHANDELBAAR bestelling exist the one with earliest date is returned")
    void whenTwoInBehandelingBestellingExists_earliestDateIsReturned() {
        // Arrange
        Bestelling bestellingInBehandeling = ObjectBuilders.getDefaultBestellingBuilder()
                .status(BestelStatusType.BEHANDELBAAR)
                .bestelNummer(1L)
                .behandelbaarSinds(LocalDateTime.now())
                .build();
        Bestelling bestellingBehandelbaar = ObjectBuilders.getDefaultBestellingBuilder()
                .status(BestelStatusType.BEHANDELBAAR)
                .bestelNummer(2L)
                .behandelbaarSinds(LocalDateTime.MAX)
                .build();
        bestellingRepository.save(bestellingInBehandeling);
        bestellingRepository.save(bestellingBehandelbaar);
        // Act
        var actual = verwerkingController.getNextBehandelbaarBestelling();
        // Assert
        assertThat(actual.getBestelNummer()).isEqualTo(1L);
        assertThat(actual.getStatus()).isEqualTo(BestelStatusType.IN_BEHANDELING);
    }

    @Test
    @DisplayName("Mark artikel is verwerkt marks the artikel verwerkt on the right order")
    void markArtikel_marksArtikelAsVerwerkt_onCorrectOrder() {
        // Arrange
        Bestelling bestelling = ObjectBuilders.getDefaultBestellingBuilder()
                .status(BestelStatusType.IN_BEHANDELING)
                .behandelbaarSinds(LocalDateTime.now())
                .build();
        bestellingRepository.save(bestelling);
        ArtikelVerwerktDto artikelVerwerktDto = ObjectBuilders.getDefaultArtikelVerwerktDto().build();
        // Act
        verwerkingController.artikelVerwerkt(artikelVerwerktDto);
        // Assert
        var actual = bestellingRepository.findById(bestelling.getBestelNummer()).orElseThrow();
        assertThat(actual.getArtikelen().stream()
                .filter(besteldArtikel -> besteldArtikel.getId() == artikelVerwerktDto.getArtikelNummer())
                .findFirst()
                .orElseThrow()
                .isVerwerkt())
                .isTrue();
    }

    @Test
    @DisplayName("If specified bestelling is not on status IN_BEHANDELING exception is thrown")
    void markArtikelVerwerkt_onBestellingNotInBehandeling_throwsIllegalStateException() {
        // Arrange
        Bestelling bestelling = ObjectBuilders.getDefaultBestellingBuilder()
                .status(BestelStatusType.BEHANDELBAAR)
                .behandelbaarSinds(LocalDateTime.now())
                .build();
        bestellingRepository.save(bestelling);
        ArtikelVerwerktDto artikelVerwerktDto = ObjectBuilders.getDefaultArtikelVerwerktDto().build();
        // Act
        // Assert
        assertThatThrownBy(() -> verwerkingController.artikelVerwerkt(artikelVerwerktDto))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName("If mark as done is called the event is send for status update")
    void markAsDone_sendsStatusVerstuurdCommand() throws JsonProcessingException {
        // Arrange
        ArgumentCaptor<BestellingStatusEvent> captor = ArgumentCaptor.forClass(BestellingStatusEvent.class);
        Bestelling bestelling = ObjectBuilders.getDefaultBestellingBuilder()
                .status(BestelStatusType.BEHANDELBAAR)
                .behandelbaarSinds(LocalDateTime.now())
                .artikelen(List.of(
                             ObjectBuilders.getDefaultBesteldArtikel().isVerwerkt(true).build(),
                             ObjectBuilders.getDefaultBesteldArtikel().isVerwerkt(true).build()
                        ))
                .build();
        bestellingRepository.save(bestelling);
        // Act
        verwerkingController.verwerkingKlaar(new BestellingKlaarDto(bestelling.getBestelNummer()));
        // Assert
        verify(bestellingStatusEventSender, times(1)).send(captor.capture());
        BestellingStatusEvent event = captor.getValue();
        assertThat(event.getBestelNummer()).isEqualTo(1L);
        assertThat(event.getTopic()).isEqualTo("Kantilever.BestellenService.BestellingVerstuurdEvent");
    }

    @Test
    @DisplayName("If Bestelling is not yet done but tried to be marked as such an exception is thrown")
    void markAsDone_withArtikelNotDone_sendsIllegalStateException() {
        // Arrange
        Bestelling bestelling = ObjectBuilders.getDefaultBestellingBuilder()
                .status(BestelStatusType.BEHANDELBAAR)
                .behandelbaarSinds(LocalDateTime.now())
                .build();
        bestellingRepository.save(bestelling);
        // Act
        // Assert
        assertThatThrownBy(() -> verwerkingController.verwerkingKlaar(BestellingKlaarDto.builder().bestelNummer(1L).build()))
                .withFailMessage("Bestelling not done")
                .isInstanceOf(ResponseStatusException.class);
    }


}
