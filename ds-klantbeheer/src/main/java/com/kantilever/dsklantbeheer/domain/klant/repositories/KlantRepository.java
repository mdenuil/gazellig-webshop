package com.kantilever.dsklantbeheer.domain.klant.repositories;


import java.util.Optional;
import com.kantilever.dsklantbeheer.domain.klant.models.Klant;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KlantRepository extends CrudRepository<Klant, Long> {
    Optional<Klant> findByEmail(String email);

    boolean existsByEmail(String email);
}
