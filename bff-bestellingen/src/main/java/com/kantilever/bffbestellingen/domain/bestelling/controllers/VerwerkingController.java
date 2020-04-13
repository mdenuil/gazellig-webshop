package com.kantilever.bffbestellingen.domain.bestelling.controllers;

import com.kantilever.bffbestellingen.domain.bestelling.dto.ArtikelVerwerktDto;
import com.kantilever.bffbestellingen.domain.bestelling.dto.BestellingKlaarDto;
import com.kantilever.bffbestellingen.domain.bestelling.models.Bestelling;
import com.kantilever.bffbestellingen.domain.bestelling.services.VerwerkingService;
import javassist.NotFoundException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

/**
 * VerwerkingController holds the endpoints for all Verwerking operations on a Bestelling. Handles marking aBestelling
 * as Klaar. Setting a specific Artikel to Verwerkt and getting the next Bestelling with
 * {@link com.kantilever.bffbestellingen.domain.bestelling.models.BestelStatusType} behandelbaar.
 */
@RestController
@Log4j2
@RequestMapping("verwerking")
public class VerwerkingController {
    private VerwerkingService verwerkingService;

    @Autowired
    public VerwerkingController(VerwerkingService verwerkingService) {
        this.verwerkingService = verwerkingService;
    }

    @GetMapping("volgendeBestelling")
    @ResponseBody
    public Bestelling getNextBehandelbaarBestelling() {
        try {
            return verwerkingService.getNextBestelling();
        } catch (NotFoundException e) {
            log.error("Bestelling not found in getNextBehandelbaarBestelling", e);
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PostMapping("artikelVerwerkt")
    @ResponseBody
    public Bestelling artikelVerwerkt(@RequestBody ArtikelVerwerktDto artikelVerwerktDto) {
        try {
            return verwerkingService.verwerkArtikel(artikelVerwerktDto);
        } catch (NotFoundException e) {
            log.error("Bestelling not found in artikelVerwerkt", e);
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage());
        }

    }

    @PostMapping("klaar")
    @ResponseBody
    public Bestelling verwerkingKlaar(@RequestBody BestellingKlaarDto bestellingKlaarDto) {
        try {
            return verwerkingService.setBestellingKlaar(bestellingKlaarDto);
        } catch (NotFoundException e) {
            log.error("Bestelling not found in verwerkingKlaar", e);
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage());
        } catch (IllegalStateException e) {
            log.error("Bestelling not done", e);
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
