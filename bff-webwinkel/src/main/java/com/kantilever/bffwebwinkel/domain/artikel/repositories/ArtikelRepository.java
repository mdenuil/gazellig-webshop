package com.kantilever.bffwebwinkel.domain.artikel.repositories;

import java.util.Collection;
import java.util.List;
import com.kantilever.bffwebwinkel.domain.artikel.models.Artikel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArtikelRepository extends CrudRepository<Artikel, Integer> {

    /**
     * This JPA Query will look for all Artikelen
     * where the field name contains the incoming parameter.
     * JPA Translates this query to a LIKE statement: "%{name}%"
     *
     * @param name as String where Upper/Lowercase is ignored
     * @return a filtered List of {@link Artikel}
     */
    List<Artikel> findAllByNaamContainingIgnoreCase(String name);

    /**
     * This JPA Query will look for all Artikelen
     * where the field beschrijving contains the incoming parameter.
     * JPA Translates this query to a LIKE statement: "%{name}%"
     *
     * @param name as String where Upper/Lowercase is ignored
     * @return a filtered List of {@link Artikel}
     */
    List<Artikel> findAllByBeschrijvingContainingIgnoreCase(String name);

}
