package com.kantilever.pcsbestellen.domain.artikel;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArtikelRepository extends CrudRepository<Artikel, Integer> {
}
