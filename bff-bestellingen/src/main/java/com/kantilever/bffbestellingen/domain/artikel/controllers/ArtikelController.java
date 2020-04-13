package com.kantilever.bffbestellingen.domain.artikel.controllers;

import java.util.List;
import com.kantilever.bffbestellingen.domain.artikel.models.Artikel;
import com.kantilever.bffbestellingen.domain.artikel.services.ArtikelService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Endpoints for {@link Artikel} objects.
 */
@RestController
@RequestMapping("artikel")
public class ArtikelController {
    private ArtikelService artikelService;

    @Autowired
    public ArtikelController(ArtikelService artikelService) {
        this.artikelService = artikelService;
    }

    /**
     * Get all available {@link Artikel} objects currently persisted.
     *
     * @return List of all Artikel objects
     */
    @GetMapping
    @ResponseBody
    public List<Artikel> getAll() {
        return artikelService.getAllArtikelen();
    }

    /**
     * Get a specific {@link Artikel} object
     *
     * @param artikelNummer id of the requested Artikel.
     * @return Artikel with artikelNummer.
     * @throws NotFoundException thrown if artikelNummer has no known Artikel.
     */
    @GetMapping("{artikelNummer}")
    @ResponseBody
    public Artikel getArtikel(@PathVariable("artikelNummer") int artikelNummer) throws NotFoundException {
        return artikelService.getArtikel(artikelNummer);
    }

}
