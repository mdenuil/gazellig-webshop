package com.kantilever.bffwebwinkel.domain.artikel.services;

import java.util.List;
import java.util.Optional;
import com.kantilever.bffwebwinkel.domain.artikel.repositories.ArtikelRepository;
import com.kantilever.bffwebwinkel.util.ObjectBuilders;
import javassist.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ArtikelServiceTest {
    @Mock
    private ArtikelRepository artikelRepository;

    private ArtikelService artikelService;

    @BeforeEach
    void init() {
        artikelService = new ArtikelService(artikelRepository);
    }

    @Test
    @DisplayName("getAllArtikelen returns List of Artikelen")
    void getAllArtikelen_returnsList() {
        // Arrange
        when(artikelRepository.findAll()).thenReturn(List.of(
                ObjectBuilders.getDefaultArtikel().build(),
                ObjectBuilders.getDefaultArtikel().artikelNummer(2).build()
        ));
        // Act
        var actual = artikelService.getAllArtikelen();
        // Assert
        assertThat(actual.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("getArtikel calls findById service method ")
    void getArtikel_callsCorrectServiceMethod() throws NotFoundException {
        // Arrange
        int artikelNummer = 1;
        when(artikelRepository.findById(artikelNummer))
                .thenReturn(Optional.of(ObjectBuilders.getDefaultArtikel().build()));
        // Act
        artikelService.getArtikel(artikelNummer);
        // Assert
        verify(artikelRepository, times(1)).findById(artikelNummer);
    }

    @Test
    @DisplayName("getArtikel returns Artikel when found")
    void getArtikel_returnsArtikel() throws NotFoundException {
        // Arrange
        int artikelNummer = 1;
        var expected = ObjectBuilders.getDefaultArtikel().build();
        when(artikelRepository.findById(artikelNummer))
                .thenReturn(Optional.of(ObjectBuilders.getDefaultArtikel().build()));
        // Act
        var actual = artikelService.getArtikel(artikelNummer);
        // Assert
        assertThat(actual).isEqualTo(expected);
        verify(artikelRepository, times(1)).findById(artikelNummer);
    }

    @Test
    @DisplayName("getArtikel throws NotFoundException when Artikel does not exist")
    void getArtikel_throwsNotFoundException() {
        // Arrange
        int artikelNummer = 1;
        when(artikelRepository.findById(artikelNummer))
                .thenReturn(Optional.empty());
        // Act
        // Assert
        assertThatThrownBy(() -> artikelService.getArtikel(artikelNummer)).isInstanceOf(NotFoundException.class);
    }

    @Test
    @DisplayName("Verhoog aantal increases aantal on found Artikel")
    void verhoogAantal_verhoogsAantal() {
        // Arrange
        var voorraadEvent = ObjectBuilders.getDefaultVoorraadEvent().build();
        when(artikelRepository.findById(voorraadEvent.getArtikelNummer()))
                .thenReturn(Optional.of(ObjectBuilders.getDefaultArtikel().build()));
        when(artikelRepository.save(any())).thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));
        // Act
        artikelService.verhoogAantal(voorraadEvent);
        // Assert
        var inOrder = inOrder(artikelRepository);
        inOrder.verify(artikelRepository, times(1)).findById(
                voorraadEvent.getArtikelNummer()
        );
        inOrder.verify(artikelRepository, times(1)).save(
                ObjectBuilders.getDefaultArtikel().aantal(10).build()
        );
    }

    @Test
    @DisplayName("Verhoog aantal fails silently when artikel not found")
    void verhoogAantal_failsSilently() {
        // Arrange
        var voorraadEvent = ObjectBuilders.getDefaultVoorraadEvent().build();
        when(artikelRepository.findById(voorraadEvent.getArtikelNummer()))
                .thenReturn(Optional.empty());
        // Act
        artikelService.verhoogAantal(voorraadEvent);
        // Assert
        verify(artikelRepository, times(0)).save(
                any()
        );
    }

    @Test
    @DisplayName("Verlaag aantal increases aantal on found Artikel")
    void verlaagAantal_verlaagsAantal() {
        // Arrange
        var voorraadEvent = ObjectBuilders.getDefaultVoorraadEvent().build();
        when(artikelRepository.findById(voorraadEvent.getArtikelNummer()))
                .thenReturn(Optional.of(ObjectBuilders.getDefaultArtikel().build()));
        when(artikelRepository.save(any())).thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));
        // Act
        artikelService.verlaagAantal(voorraadEvent);
        // Assert
        var inOrder = inOrder(artikelRepository);
        inOrder.verify(artikelRepository, times(1)).findById(
                voorraadEvent.getArtikelNummer()
        );
        inOrder.verify(artikelRepository, times(1)).save(
                ObjectBuilders.getDefaultArtikel().aantal(-10).build()
        );
    }

    @Test
    @DisplayName("Verlaag aantal fails silently when artikel not found")
    void verlaagAantal_failsSilently() {
        // Arrange
        var voorraadEvent = ObjectBuilders.getDefaultVoorraadEvent().build();
        when(artikelRepository.findById(voorraadEvent.getArtikelNummer()))
                .thenReturn(Optional.empty());
        // Act
        artikelService.verlaagAantal(voorraadEvent);
        // Assert
        verify(artikelRepository, times(0)).save(
                any()
        );
    }
}