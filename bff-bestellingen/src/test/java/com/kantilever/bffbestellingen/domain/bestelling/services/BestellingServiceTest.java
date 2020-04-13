package com.kantilever.bffbestellingen.domain.bestelling.services;

import com.kantilever.bffbestellingen.domain.bestelling.amqp.events.bestellingstatus.BestellingStatusEvent;
import com.kantilever.bffbestellingen.domain.bestelling.models.BestelStatusType;
import com.kantilever.bffbestellingen.domain.bestelling.models.Bestelling;
import com.kantilever.bffbestellingen.domain.bestelling.repositories.BestellingRepository;
import com.kantilever.bffbestellingen.domain.bestelling.services.BestellingService;
import com.kantilever.bffbestellingen.util.ObjectBuilders;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BestellingServiceTest {
    @Mock
    private BestellingRepository bestellingRepository;

    private Clock clock = Clock.fixed(Instant.now(), ZoneId.of("Europe/Paris"));
    private Bestelling.BestellingBuilder bestelling;
    private BestellingService bestellingService;


    @BeforeEach
    void init() {
        bestelling = ObjectBuilders.getDefaultBestellingBuilder();
        bestellingService = new BestellingService(bestellingRepository, clock);
    }

    @ParameterizedTest
    @EnumSource(
            value = BestelStatusType.class,
            names = {"BEHANDELBAAR"},
            mode = EnumSource.Mode.EXCLUDE
    )
    @DisplayName("AddNewBestelling when BestelStatus is not Behandelbaar the Bestelling is persisted normally")
    void addNewBestellingHandlesNotBehandelbaarBestellingen(BestelStatusType bestelStatusType) {
        // Arrange
        when(bestellingRepository.save(bestelling.status(bestelStatusType).build()))
                .thenReturn(bestelling.status(bestelStatusType).build());
        // Act
        var actual = bestellingService.addNewBestelling((bestelling.status(bestelStatusType)).build());
        // Assert
        verify(bestellingRepository, times(1))
                .save(bestelling.status(bestelStatusType).build());
        assertThat(actual.getStatus().equals(bestelStatusType)).isTrue();
    }

    @Test
    @DisplayName("AddNewBestelling when BestelStatus is BEHANDELBAAR has its behandelbaarSinds time set")
    void addNewBestellingHandlesBehandelbaarBestellingen() {
        // Arrange
        when(bestellingRepository.save(bestelling.behandelbaarSinds(LocalDateTime.now(clock)).build()))
                .thenReturn(bestelling.behandelbaarSinds(LocalDateTime.now(clock)).build());
        // Act
        var actual = bestellingService.addNewBestelling(bestelling.status(BestelStatusType.BEHANDELBAAR).build());
        // Assert
        verify(bestellingRepository, times(1))
                .save(bestelling
                        .status(BestelStatusType.BEHANDELBAAR)
                        .behandelbaarSinds(LocalDateTime.now(clock))
                        .build());
        assertThat(actual.getStatus().equals(BestelStatusType.BEHANDELBAAR)).isTrue();

    }

    @Test
    @DisplayName("Find all bestellingen calls repository and returns a list of bestellingen")
    void findAllBestellingReturnsCorrectList() {
        // Arrange
        var bestellingOne = ObjectBuilders.getDefaultBestellingBuilder().bestelNummer(1L).build();
        var bestellingTwo = ObjectBuilders.getDefaultBestellingBuilder().bestelNummer(1L).build();
        var bestellingThree = ObjectBuilders.getDefaultBestellingBuilder().bestelNummer(1L).build();

        when(bestellingRepository.findAll()).thenReturn(List.of(
                bestellingOne, bestellingTwo, bestellingThree
        ));
        // Act
        var actual = bestellingService.findAllBestellingen();
        // Assert
        assertThat(actual.contains(bestellingOne)).isTrue();
        assertThat(actual.contains(bestellingTwo)).isTrue();
        assertThat(actual.contains(bestellingThree)).isTrue();
        verify(bestellingRepository, times(1)).findAll();
    }

    @ParameterizedTest
    @EnumSource(BestelStatusType.class)
    @DisplayName("SetBestellingStatus persists the correct status on the correct Bestelling")
    void bestelStatusTypesGetSetCorrectly(BestelStatusType bestelStatusType) {
        // Arrange
        long id = 1;
        when(bestellingRepository.findById(id))
                .thenReturn(Optional.of(bestelling
                        .status(bestelStatusType)
                        .build())
                );
        // Act
        bestellingService.setBestellingStatus(id, bestelStatusType);
        // Assert
        verify(bestellingRepository, times(1))
                .save(bestelling
                        .bestelNummer(1L)
                        .status(bestelStatusType)
                        .build());
    }

    @ParameterizedTest
    @EnumSource(BestelStatusType.class)
    @DisplayName("SetBestellingStatus does nothing when id can't be found Bestelling")
    void bestelStatusHandlesUnknownIdsGracefully(BestelStatusType bestelStatusType) {
        // Arrange
        long id = 1;
        when(bestellingRepository.findById(id))
                .thenReturn(Optional.empty());
        // Act
        bestellingService.setBestellingStatus(id, bestelStatusType);
        // Assert
        verify(bestellingRepository, times(0))
                .save(any());
    }

    @Test
    @DisplayName("SetStatusBehandelbaar sets the correct datetime for when the status is updated")
    void setStatusBehandelbaarSetsTimeAndUpdatesStatus() {
        // Arrange
        long id = 1;
        when(bestellingRepository.findById(id))
                .thenReturn(Optional.of(bestelling
                        .behandelbaarSinds(null)
                        .build())
                );
        // Act
        bestellingService.setBehandelbaarSindsNow(id);
        // Assert
        InOrder inOrder = inOrder(bestellingRepository);
        inOrder.verify(bestellingRepository).findById(1L);
        inOrder.verify(bestellingRepository)
                .save(bestelling
                        .bestelNummer(1L)
                        .behandelbaarSinds(LocalDateTime.now(clock))
                        .build());
    }
}