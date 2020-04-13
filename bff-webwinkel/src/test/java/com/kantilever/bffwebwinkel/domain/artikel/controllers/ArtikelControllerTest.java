package com.kantilever.bffwebwinkel.domain.artikel.controllers;

import java.util.List;
import com.kantilever.bffwebwinkel.domain.artikel.services.ArtikelService;
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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ArtikelControllerTest {
    @Mock
    private ArtikelService artikelService;
    private ArtikelController artikelController;

    @BeforeEach
    void init() {
        artikelController = new ArtikelController(artikelService);
    }

    @Test
    @DisplayName("getAll returns list of Artikelen")
    void getAll_returnsList() {
        // Arrange
        when(artikelService.getAllArtikelen()).thenReturn(List.of(
                ObjectBuilders.getDefaultArtikel().build(),
                ObjectBuilders.getDefaultArtikel().artikelNummer(2).build()
        ));
        // Act
        var actual = artikelController.getAll();
        // Assert
        assertThat(actual.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("getArtikel returns artikel for artikelNummer")
    void getArtikel_returnsArtikel() throws NotFoundException {
        int artikelNummer = 1;
        var expected = ObjectBuilders.getDefaultArtikel().build();
        when(artikelService.getArtikel(artikelNummer))
                .thenReturn(expected);
        // Act
        var actual = artikelController.getArtikel(artikelNummer);
        // Assert
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("getArtikel throws NotFoundException")
    void getArtikel_throwsNotFoundException() throws NotFoundException {
        // Arrange
        int artikelNummer = 1;
        when(artikelService.getArtikel(artikelNummer)).thenThrow(NotFoundException.class);
        // Act
        // Assert
        assertThatThrownBy(() -> artikelController.getArtikel(artikelNummer)).isInstanceOf(NotFoundException.class);
    }
}