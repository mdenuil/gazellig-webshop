package com.kantilever.bffbestellingen.domain.bestelling.controllers;

import com.kantilever.bffbestellingen.domain.bestelling.models.Bestelling;
import java.util.List;
import com.kantilever.bffbestellingen.domain.bestelling.services.BestellingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * BestellingController holds the endpoints for operations on Bestellingen.
 */
@RestController
@RequestMapping("bestelling")
public class BestellingController {
    private BestellingService bestellingService;

    @Autowired
    public BestellingController(BestellingService bestellingService) {
        this.bestellingService = bestellingService;
    }

    @GetMapping
    @ResponseBody
    public List<Bestelling> findAll() {
        return bestellingService.findAllBestellingen();
    }
}
