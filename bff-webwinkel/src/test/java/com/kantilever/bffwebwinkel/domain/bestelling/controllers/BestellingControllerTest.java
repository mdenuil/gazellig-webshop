package com.kantilever.bffwebwinkel.domain.bestelling.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kantilever.bffwebwinkel.domain.bestelling.amqp.senders.BestellingEventSenderService;
import com.kantilever.bffwebwinkel.domain.bestelling.services.BestellingService;
import com.kantilever.bffwebwinkel.domain.klant.models.EKlantSoort;
import com.kantilever.bffwebwinkel.domain.klant.models.Klant;
import com.kantilever.bffwebwinkel.domain.klant.models.KlantSoort;
import com.kantilever.bffwebwinkel.security.services.UserDetailsImpl;
import com.kantilever.bffwebwinkel.util.ObjectBuilders;
import java.util.List;
import java.util.Set;
import javassist.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BestellingControllerTest {
    @Mock
    private BestellingEventSenderService bestellingEventSenderService;
    @Mock
    private BestellingService bestellingService;
    @Mock
    private Authentication authentication;

    private BestellingController bestellingController;

    @BeforeEach
    void init() {
        bestellingController = new BestellingController(bestellingEventSenderService, bestellingService);
    }


    @Test
    @DisplayName("addNewBestelling calls correct method on EventSenderService and returns HttpEntityOk with payload")
    void addNewBestelling_callsCorrectService_andReturnsHttpEntityOk() throws JsonProcessingException {
        // Arrange
        var klantId = 1L;
        var bestellingDto = ObjectBuilders.getDefaultBestellingDtoBuilder().build();
        when(authentication.getPrincipal()).thenReturn(UserDetailsImpl.build(Klant.builder()
                .klantSoort(Set.of(KlantSoort.from(EKlantSoort.PARTICULIER)))
                .klantNummer(1L)
                .build()));
        // Act
        ResponseEntity<?> responseEntity = bestellingController.addNewBestelling(bestellingDto, authentication);
        // Assert
        verify(bestellingEventSenderService, times(1))
                .sendBestellingGeplaatstEvent(bestellingDto, klantId);
        assertThat(responseEntity.getBody()).isEqualTo(bestellingDto);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("findAllByUser calls correct method on EventSenderService and returns HttpEntityOk with payload")
    void findAllByUser_callsCorrectService_andReturnsHttpEntityOk() {
        var klantId = 1L;
        var bestellingen = List.of(
                ObjectBuilders.getDefaultBestellingBuilder().bestelNummer(1L).build(),
                ObjectBuilders.getDefaultBestellingBuilder().bestelNummer(2L).build());
        when(authentication.getPrincipal()).thenReturn(UserDetailsImpl.build(Klant.builder()
                .klantSoort(Set.of(KlantSoort.from(EKlantSoort.PARTICULIER)))
                .klantNummer(1L)
                .build()));
        when(bestellingService.findAllBestellingenForKlant(klantId)).thenReturn(bestellingen);
        // Act
        ResponseEntity<?> responseEntity = bestellingController.findAllByUser(authentication);
        // Assert
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isEqualTo(bestellingen);
    }
}