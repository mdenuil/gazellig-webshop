package com.kantilever.pcsbestellen.domain.artikel;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("artikel")
public class ArtikelController {
    private ArtikelService artikelService;

    @Autowired
    public ArtikelController(ArtikelService artikelService) {
        this.artikelService = artikelService;
    }

    @GetMapping
    public List<Artikel> getAll() {
        return this.artikelService.getAllArtikelen();
    }
}
