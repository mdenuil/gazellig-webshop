package com.kantilever.bffwebwinkel.domain.bestelling.repositories;

import com.kantilever.bffwebwinkel.domain.bestelling.models.Bestelling;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BestellingRepository extends CrudRepository<Bestelling, Long> {
    List<Bestelling> findAllByKlantNummer(Long klantNummer);
}
