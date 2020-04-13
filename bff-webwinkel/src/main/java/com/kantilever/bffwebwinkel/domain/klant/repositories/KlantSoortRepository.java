package com.kantilever.bffwebwinkel.domain.klant.repositories;

import com.kantilever.bffwebwinkel.domain.klant.models.EKlantSoort;
import com.kantilever.bffwebwinkel.domain.klant.models.KlantSoort;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KlantSoortRepository extends CrudRepository<KlantSoort, Long> {
    Optional<KlantSoort> findByNaam(EKlantSoort naam);
}
