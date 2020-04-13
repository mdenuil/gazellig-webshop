package com.kantilever.bffbestellingen.domain.bestelling.controllers;

import com.kantilever.bffbestellingen.domain.bestelling.dto.ArtikelVerwerktDto;
import com.kantilever.bffbestellingen.domain.bestelling.dto.BestellingKlaarDto;
import com.kantilever.bffbestellingen.domain.bestelling.services.VerwerkingService;
import com.kantilever.bffbestellingen.util.ObjectBuilders;
import javassist.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VerwerkingControllerTest {
    @Mock
    private VerwerkingService verwerkingsService;

    private VerwerkingController verwerkingController;

    @BeforeEach
    void init() {
        this.verwerkingController = new VerwerkingController(verwerkingsService);
    }

    @Test
    @DisplayName("getNextBehandelbaarBestelling calls verwerkingsService for new bestelling")
    void getNextBehandelbaarBestelling_callsRightServiceMethod() throws NotFoundException {
        // Arrange
        var expected = ObjectBuilders.getDefaultBestellingBuilder().build();
        when(verwerkingsService.getNextBestelling())
                .thenReturn(ObjectBuilders.getDefaultBestellingBuilder().build());
        // Act
        var actual = verwerkingController.getNextBehandelbaarBestelling();
        // Assert
        assertThat(actual).isEqualTo(expected);
        verify(verwerkingsService, times(1)).getNextBestelling();
    }

    @Test
    @DisplayName("getNextBehandelbaarBestelling throws ResponseStatusException.NOT_FOUND when no new bestelling is available")
    void getNextBehandelbaarBestelling_throwsNotFound() throws NotFoundException {
        // Arrange
        when(verwerkingsService.getNextBestelling()).thenThrow(new NotFoundException("No new bestelling"));
        // Act
        // Assert
        assertThatThrownBy(() -> verwerkingController.getNextBehandelbaarBestelling())
                .isInstanceOf(ResponseStatusException.class);
        assertThatThrownBy(() -> verwerkingController.getNextBehandelbaarBestelling())
                .hasMessageContaining("404");
    }

    @Test
    @DisplayName("artikelVerwerkt calls verwerkingsService with artikelVerwerktDto")
    void artikelVerwerkt_callsRightServiceMethod() throws NotFoundException {
        // Arrange
        var artikelVerwerktDto = ObjectBuilders.getDefaultArtikelVerwerktDto().build();
        // Act
        verwerkingController.artikelVerwerkt(artikelVerwerktDto);
        // Assert
        verify(verwerkingsService, times(1))
                .verwerkArtikel(artikelVerwerktDto);
    }

    @Test
    @DisplayName("artikelVerwerkt throws ResponseStatusException.NOT_FOUND when no bestelling is found")
    void artikelVerwerkt_throwsNotFound() throws NotFoundException {
        // Arrange
        ArtikelVerwerktDto artikelVerwerktDto = ArtikelVerwerktDto.builder().build();
        when(verwerkingsService.verwerkArtikel(artikelVerwerktDto)).thenThrow(new NotFoundException("No bestelling found"));
        // Act
        // Assert
        assertThatThrownBy(() -> verwerkingController.artikelVerwerkt(artikelVerwerktDto))
                .isInstanceOf(ResponseStatusException.class);
        assertThatThrownBy(() -> verwerkingController.artikelVerwerkt(artikelVerwerktDto))
                .hasMessageContaining("404");
    }

    @Test
    @DisplayName("verwerkingKlaar calls verwerkingsService with bestellingKlaarDto")
    void verwerkingDone_callsRightServiceMethod() throws NotFoundException {
        // Arrange
        var bestellingKlaarDto = BestellingKlaarDto.builder().bestelNummer(1).build();
        // Act
        verwerkingController.verwerkingKlaar(bestellingKlaarDto);
        // Assert
        verify(verwerkingsService, times(1))
                .setBestellingKlaar(bestellingKlaarDto);
    }

    @Test
    @DisplayName("verwerkingKlaar throws ResponseStatusException.NOT_FOUND when no bestelling is found")
    void verwerkingKlaar_throwsNotFound() throws NotFoundException {
        // Arrange
        var bestellingKlaarDto = BestellingKlaarDto.builder().bestelNummer(1).build();
        when(verwerkingsService.setBestellingKlaar(bestellingKlaarDto))
                .thenThrow(new NotFoundException("No bestelling found"));
        // Act
        // Assert
        assertThatThrownBy(() -> verwerkingController.verwerkingKlaar(bestellingKlaarDto))
                .isInstanceOf(ResponseStatusException.class);
        assertThatThrownBy(() -> verwerkingController.verwerkingKlaar(bestellingKlaarDto))
                .hasMessageContaining("404");
    }

    @Test
    @DisplayName("verwerkingKlaar throws ResponseStatusException.BAD_REQUEST when bestelling isn't done")
    void verwerkingKlaar_throwsBadRequest() throws NotFoundException {
        // Arrange
        var bestellingKlaarDto = BestellingKlaarDto.builder().bestelNummer(1).build();
        when(verwerkingsService.setBestellingKlaar(bestellingKlaarDto))
                .thenThrow(new IllegalStateException("Bestelling not done"));
        // Act
        // Assert
        assertThatThrownBy(() -> verwerkingController.verwerkingKlaar(bestellingKlaarDto))
                .isInstanceOf(ResponseStatusException.class);
        assertThatThrownBy(() -> verwerkingController.verwerkingKlaar(bestellingKlaarDto))
                .hasMessageContaining("400");
    }
}

