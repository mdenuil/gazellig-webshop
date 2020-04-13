package com.kantilever.pcsbestellen.domain.artikel;

import com.google.common.collect.Lists;
import com.kantilever.pcsbestellen.domain.artikel.amqp.events.ArtikelEvent;
import com.kantilever.pcsbestellen.domain.artikel.amqp.events.VoorraadEvent;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class ArtikelService {
    private ArtikelRepository artikelRepository;

    public ArtikelService(ArtikelRepository artikelRepository) {
        this.artikelRepository = artikelRepository;
    }

    public Optional<Artikel> getArtikel(Integer id) {
        return artikelRepository.findById(id);
    }

    public List<Artikel> getAllArtikelen() {
        return Lists.newArrayList(this.artikelRepository.findAll());
    }

    public void addArtikel(ArtikelEvent artikelToegevoegdDto) {
        var artikel = Artikel.from(artikelToegevoegdDto);
        artikelRepository.save(artikel);
    }

    public void verhoogAantal(VoorraadEvent voorraadVeranderdDto) {
        artikelRepository
                .findById(voorraadVeranderdDto.getArtikelNummer())
                .ifPresent(artikel -> {
                    artikel.increaseAantal(voorraadVeranderdDto.getAantal());
                    artikelRepository.save(artikel);
                });
    }

    public void verlaagAantal(VoorraadEvent voorraadVeranderdDto) {
        artikelRepository
                .findById(voorraadVeranderdDto.getArtikelNummer())
                .ifPresent(artikel -> {
                    artikel.decreaseAantal(voorraadVeranderdDto.getAantal());
                    artikelRepository.save(artikel);
                });
    }
}
