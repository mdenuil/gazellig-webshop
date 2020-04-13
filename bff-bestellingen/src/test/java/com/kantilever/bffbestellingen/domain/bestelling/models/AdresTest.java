package com.kantilever.bffbestellingen.domain.bestelling.models;

import com.kantilever.bffbestellingen.domain.bestelling.dto.AdresDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class AdresTest {

    @Test
    @DisplayName("Static factory  method from(AdresDto) builds correct Adres")
    void fromAdresDtoStaticFactory_buildsCorrectAdres() {
        // Arrange
        var adresDto = AdresDto.builder()
                .huisnummer("2A")
                .postcode("0000AZ")
                .straatnaam("Straat")
                .woonplaats("Plaats")
                .build();
        // Act
        var actual = Adres.from(adresDto);
        // Assert
        assertThat(actual.getHuisnummer()).isEqualTo(adresDto.getHuisnummer());
        assertThat(actual.getPostcode()).isEqualTo(adresDto.getPostcode());
        assertThat(actual.getStraatnaam()).isEqualTo(adresDto.getStraatnaam());
        assertThat(actual.getWoonplaats()).isEqualTo(adresDto.getWoonplaats());
        assertThat(actual.getId()).isNull();
    }
}