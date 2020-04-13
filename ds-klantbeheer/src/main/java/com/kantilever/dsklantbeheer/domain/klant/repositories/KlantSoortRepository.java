package com.kantilever.dsklantbeheer.domain.klant.repositories;


import java.util.Optional;
import com.kantilever.dsklantbeheer.domain.klant.models.EKlantSoort;
import com.kantilever.dsklantbeheer.domain.klant.models.KlantSoort;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KlantSoortRepository extends CrudRepository<KlantSoort, Long> {
    Optional<KlantSoort> findByNaam(EKlantSoort naam);
}
