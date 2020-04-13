package com.kantilever.bffbestellingen.domain.bestelling.services;

import com.kantilever.bffbestellingen.domain.bestelling.amqp.senders.BestellingEventSenderService;
import com.kantilever.bffbestellingen.domain.bestelling.dto.BestellingKlaarDto;
import com.kantilever.bffbestellingen.domain.bestelling.models.BestelStatusType;
import com.kantilever.bffbestellingen.domain.bestelling.repositories.BestellingRepository;
import com.kantilever.bffbestellingen.util.ObjectBuilders;
import java.util.List;
import java.util.Optional;
import javassist.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VerwerkingServiceTest {
    @Mock
    private BestellingRepository bestellingRepository;
    @Mock
    private BestellingEventSenderService bestellingEventSenderService;

    private VerwerkingService verwerkingService;

    @BeforeEach
    void init() {
        verwerkingService = new VerwerkingService(bestellingRepository, bestellingEventSenderService);
    }

    @Test
    @DisplayName("getNextBestelling returns open bestelling if one is available")
    void getNextBestelling_returnsOpenBestelling() throws NotFoundException {
        // Arrange
        when(bestellingRepository.findFirstByStatusOrderByBehandelbaarSindsDesc(BestelStatusType.IN_BEHANDELING))
                .thenReturn(Optional.of(ObjectBuilders.getDefaultBestellingBuilder().build()));
        // Act
        var actual = verwerkingService.getNextBestelling();
        // Assert
        assertThat(actual).isEqualTo(ObjectBuilders.getDefaultBestellingBuilder().build());
    }

    @Test
    @DisplayName("getNextBestelling returns new Behandelbaar bestelling if no open ones are available")
    void getNextBestelling_returnsBehandelbaarBestelling() throws NotFoundException {
        // Arrange
        when(bestellingRepository.findFirstByStatusOrderByBehandelbaarSindsDesc(BestelStatusType.IN_BEHANDELING))
                .thenReturn(Optional.empty());
        when(bestellingRepository.findFirstByStatusOrderByBehandelbaarSindsDesc(BestelStatusType.BEHANDELBAAR))
                .thenReturn(Optional.of(ObjectBuilders.getDefaultBestellingBuilder().build()));
        // Act
        var actual = verwerkingService.getNextBestelling();
        // Assert
        assertThat(actual.getStatus()).isEqualTo(BestelStatusType.IN_BEHANDELING);
        verify(bestellingRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("getNextBestelling throws NotFoundException when no new is available")
    void getNextBestelling_throwsNotFoundException_whenNoneAvailable() {
        // Arrange
        when(bestellingRepository.findFirstByStatusOrderByBehandelbaarSindsDesc(BestelStatusType.IN_BEHANDELING))
                .thenReturn(Optional.empty());
        // Act
        when(bestellingRepository.findFirstByStatusOrderByBehandelbaarSindsDesc(BestelStatusType.BEHANDELBAAR))
                .thenReturn(Optional.empty());
        // Assert
        assertThatThrownBy(() -> verwerkingService.getNextBestelling()).hasMessage("No new bestelling");
        assertThatThrownBy(() -> verwerkingService.getNextBestelling()).isInstanceOf(NotFoundException.class);
    }

    @Test
    @DisplayName("verwerkArtikel if bestelling is not IN_BEHANDELING status IllegalStateException is thrown")
    void verwerkArtikel_throwsIllegalStateException() {
        // Arrange
        when(bestellingRepository.findById(anyLong()))
                .thenReturn(Optional.of(ObjectBuilders.getDefaultBestellingBuilder()
                        .status(BestelStatusType.BEHANDELBAAR)
                        .build()));
        var artikelDto = ObjectBuilders.getDefaultArtikelVerwerktDto().build();
        // Act
        // Assert
        assertThatThrownBy(() -> verwerkingService.verwerkArtikel(artikelDto)).hasMessage("Bestelling not in process");
        assertThatThrownBy(() -> verwerkingService.verwerkArtikel(artikelDto)).isInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName("verwerkArtikel if no bestelling can be found throws NotFoundException")
    void verwerkARtikel_throwsNotFoundException() {
        // Arrange
        when(bestellingRepository.findById(anyLong()))
                .thenReturn(Optional.empty());
        var artikelDto = ObjectBuilders.getDefaultArtikelVerwerktDto().build();

        // Act
        // Assert
        assertThatThrownBy(() -> verwerkingService.verwerkArtikel(artikelDto)).hasMessage("No such bestelling");
        assertThatThrownBy(() -> verwerkingService.verwerkArtikel(artikelDto)).isInstanceOf(NotFoundException.class);
    }

    @Test
    @DisplayName("verwerkArtikel sets correct Artikel status to Verwerkt")
    void verwerkArtikel_updatesArtikelStatus() throws NotFoundException {
        // Arrange
        when(bestellingRepository.findById(anyLong()))
                .thenReturn(Optional.of(ObjectBuilders.getDefaultBestellingBuilder()
                        .status(BestelStatusType.IN_BEHANDELING)
                        .build()));
        when(bestellingRepository.save(any()))
                .thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));
        var artikelDto = ObjectBuilders.getDefaultArtikelVerwerktDto().build();
        // Act
        var actual = verwerkingService.verwerkArtikel(artikelDto);
        var actualArtikelen = actual.getArtikelen();
        // Assert
        assertThat(actualArtikelen.get(0).isVerwerkt()).isTrue();
    }

    @Test
    @DisplayName("setBestellingKlaar if bestelling is not done throws IllegalStateException")
    void bestellingDone_throwsIllegalStateException() {
        // Arrange
        when(bestellingRepository.findById(anyLong()))
                .thenReturn(Optional.of(ObjectBuilders.getDefaultBestellingBuilder()
                        .status(BestelStatusType.IN_BEHANDELING)
                        .build()));
        var bestellingKlaarDto = BestellingKlaarDto.builder().bestelNummer(1).build();
        // Act
        // Assert
        assertThatThrownBy(() -> verwerkingService.setBestellingKlaar(bestellingKlaarDto)).hasMessage("Bestelling is not done");
        assertThatThrownBy(() -> verwerkingService.setBestellingKlaar(bestellingKlaarDto)).isInstanceOf(IllegalStateException.class);

    }

    @Test
    @DisplayName("setBestellingKlaar if no bestelling can be found throws NotFoundException")
    void bestellingDone_throwsNotFoundException() {
        // Arrange
        when(bestellingRepository.findById(anyLong()))
                .thenReturn(Optional.empty());
        var bestellingKlaarDto = BestellingKlaarDto.builder().bestelNummer(1).build();
        // Act
        // Assert
        assertThatThrownBy(() -> verwerkingService.setBestellingKlaar(bestellingKlaarDto)).hasMessage("No such bestelling");
        assertThatThrownBy(() -> verwerkingService.setBestellingKlaar(bestellingKlaarDto)).isInstanceOf(NotFoundException.class);
    }

    @Test
    @DisplayName("setBestellingKlaar set status verstuurd and persists")
    void bestellingDone_setsStatusVerstuurd() throws NotFoundException {
        // Arrange
        when(bestellingRepository.findById(anyLong()))
                .thenReturn(Optional.of(ObjectBuilders.getDefaultBestellingBuilder()
                        .status(BestelStatusType.IN_BEHANDELING)
                        .artikelen(List.of(
                                ObjectBuilders.getDefaultBesteldArtikel().isVerwerkt(true).build(),
                                ObjectBuilders.getDefaultBesteldArtikel().artikelNummer(2).isVerwerkt(true).build()))
                        .build()));
        when(bestellingRepository.save(any())).thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));
        var bestellingKlaarDto = BestellingKlaarDto.builder().bestelNummer(1).build();
        // Act
        var bestelling = verwerkingService.setBestellingKlaar(bestellingKlaarDto);
        // Assert
        assertThat(bestelling.getStatus()).isEqualTo(BestelStatusType.VERSTUURD);
        verify(bestellingRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("setBestellingKlaar calls sendBestellingVerstuurd on EventService")
    void bestellingDone_callsEventService() throws NotFoundException {
        // Arrange
        when(bestellingRepository.findById(anyLong()))
                .thenReturn(Optional.of(ObjectBuilders.getDefaultBestellingBuilder()
                        .status(BestelStatusType.IN_BEHANDELING)
                        .artikelen(List.of(
                                ObjectBuilders.getDefaultBesteldArtikel().isVerwerkt(true).build(),
                                ObjectBuilders.getDefaultBesteldArtikel().artikelNummer(2).isVerwerkt(true).build()))
                        .build()));
        var bestellingKlaarDto = BestellingKlaarDto.builder().bestelNummer(1).build();
        // Act
        var bestelling = verwerkingService.setBestellingKlaar(bestellingKlaarDto);
        // Assert
        verify(bestellingEventSenderService, times(1)).sendBestellingVerstuurd(1L);
    }
}
