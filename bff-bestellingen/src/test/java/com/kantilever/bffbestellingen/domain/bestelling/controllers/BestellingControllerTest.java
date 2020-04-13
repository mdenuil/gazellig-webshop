package com.kantilever.bffbestellingen.domain.bestelling.controllers;

import com.kantilever.bffbestellingen.domain.bestelling.controllers.BestellingController;
import com.kantilever.bffbestellingen.domain.bestelling.services.BestellingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BestellingControllerTest {
    @Mock
    private BestellingService bestellingService;
    private BestellingController bestellingController;

    @BeforeEach
    void init() {
        bestellingController = new BestellingController(bestellingService);
    }

    @Test
    @DisplayName("findAll delegates to the bestellingService")
    void findAll_callsBestellingService_withFindAll() {
        // Arrange
        // Act
        bestellingController.findAll();
        // Assert
        verify(bestellingService, times(1)).findAllBestellingen();
    }
}