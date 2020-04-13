package com.kantilever.dsklantbeheer.domain.klant.services;

import java.math.BigDecimal;
import com.kantilever.dsklantbeheer.domain.klant.models.Klant;
import com.kantilever.dsklantbeheer.domain.klant.repositories.KlantRepository;
import com.kantilever.dsklantbeheer.util.ObjectBuilders;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class KlantServiceTest {
    @Mock
    private KlantRepository klantRepository;
    private KlantService klantService;

    @BeforeEach
    void init() {
        klantService = new KlantService(klantRepository);
    }

    @Test
    @DisplayName("Default krediet is set while persisting a new Klant object")
    void defaultKredietIsSetOnPersisting() {
        // Arrange
        var expected = ObjectBuilders.getDefaultKlant().build();
        var klantToAdd = ObjectBuilders.getDefaultKlant().krediet(null).klantNummer(null).build();
        when(klantRepository.save(klantToAdd)).thenAnswer(invocationOnMock -> {
            Klant klant = invocationOnMock.getArgument(0);
            klant.setKlantNummer(1L);
            return klant;
        });
        // Act
        var actual = klantService.addNewKlant(klantToAdd);
        // Assert
        assertThat(actual).isEqualTo(expected);
        assertThat(actual.getKrediet()).isEqualTo(BigDecimal.valueOf(500));
    }
}