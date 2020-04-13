package com.kantilever.bffbestellingen.domain.bestelling.repositories;

import com.kantilever.bffbestellingen.domain.bestelling.models.BestelStatusType;
import com.kantilever.bffbestellingen.domain.bestelling.models.Bestelling;
import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BestellingRepository extends CrudRepository<Bestelling, Long> {

    /**
     * Overwrite findAll to return a List instead of an Iterator.
     *
     * @return List of all Bestelling objects
     */
    List<Bestelling> findAll();

    /**
     * Return the first {@link BestelStatusType} ordered by Behandelbaar date set
     *
     * @param status Status to get Bestelling by.
     * @return Optional of Bestelling.
     */
    Optional<Bestelling> findFirstByStatusOrderByBehandelbaarSindsDesc(BestelStatusType status);
}

