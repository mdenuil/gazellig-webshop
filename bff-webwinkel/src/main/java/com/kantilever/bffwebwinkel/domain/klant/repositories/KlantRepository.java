package com.kantilever.bffwebwinkel.domain.klant.repositories;

import com.kantilever.bffwebwinkel.domain.klant.models.Klant;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KlantRepository extends CrudRepository<Klant, Long> {
    Optional<Klant> findByEmail(String email);

    boolean existsByEmail(String email);
}
