package com.kantilever.pcsbestellen.domain.bestelling;

import com.kantilever.pcsbestellen.domain.bestelling.models.BestelStatusType;
import com.kantilever.pcsbestellen.domain.bestelling.models.Bestelling;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BestellingRepository extends CrudRepository<Bestelling, Long> {

    Iterable<Bestelling> findAllByStatus(BestelStatusType statusType);

}
