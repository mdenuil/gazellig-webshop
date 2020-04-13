package com.kantilever.bffwebwinkel.domain.artikel.controllers;

import java.util.List;
import java.util.Set;
import com.kantilever.bffwebwinkel.domain.artikel.services.ArtikelService;
import com.kantilever.bffwebwinkel.domain.artikel.models.Artikel;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Endpoints for {@link Artikel} objects.
 */
@CrossOrigin(origins = "*")
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
    public List<Artikel> getAll() {
        return this.artikelService.getAllArtikelen();
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

    /**
     * This endpoint filters Artikelen on Name and Description
     * If there are results on both fields, the service will sort them so
     * search results on 'name' will be first in the list and
     * search results on 'description' will be second
     *
     * @param key as String where Upper/Lowercase is ignored
     * @return a filtered List of {@link Artikel}
     */
    @GetMapping("/filter")
    public Set<Artikel> getFilteredArtikelenByName(@RequestParam String key) {
        return artikelService.getArtikelenFilteredByNameAndDescription(key);
    }
}
