package com.kantilever.bffbestellingen.domain.bestelling.models;

import com.kantilever.bffbestellingen.domain.bestelling.dto.BesteldArtikelDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;


class BesteldArtikelTest {

    @Test
    @DisplayName("Static factory from(BesteldArtikelDto) builds correct BesteldArtikel")
    void name() {
        // Arrange
        var besteldArtikelDto = BesteldArtikelDto.builder()
                .artikelNummer(1)
                .aantal(2)
                .build();
        // Act
        var besteldArtikel = BesteldArtikel.from(besteldArtikelDto);
        // Assert
        assertThat(besteldArtikel.getAantal()).isEqualTo(besteldArtikelDto.getAantal());
        assertThat(besteldArtikel.getArtikelNummer()).isEqualTo(besteldArtikelDto.getArtikelNummer());
    }
}