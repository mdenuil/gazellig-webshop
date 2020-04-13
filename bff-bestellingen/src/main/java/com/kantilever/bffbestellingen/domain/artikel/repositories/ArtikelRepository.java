package com.kantilever.bffbestellingen.domain.artikel.repositories;

import com.kantilever.bffbestellingen.domain.artikel.models.Artikel;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArtikelRepository extends CrudRepository<Artikel, Integer> {

    /**
     * Overwrite findAll to return a List instead of an Iterator.
     *
     * @return List of all Artikel objects
     */
    List<Artikel> findAll();
}
