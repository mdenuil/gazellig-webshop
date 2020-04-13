package com.kantilever.dsklantbeheer.domain.klant.models;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

class KlantSoortTest {

    @Test
    @DisplayName("Static factory method creates KlantSoort from EKlantSoort.Particulier")
    void fromEKlantSoortParticulier_createsCorrectKlantSoort() {
        // Arrange
        // Act
        var klantSoort = KlantSoort.from(EKlantSoort.PARTICULIER);
        // Assert
        assertThat(klantSoort.getNaam()).isEqualTo(EKlantSoort.PARTICULIER);
    }
}