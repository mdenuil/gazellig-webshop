package com.kantilever.bffwebwinkel.domain.winkelmandje;

import com.kantilever.bffwebwinkel.domain.winkelmandje.models.Winkelmandje;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WinkelmandjeRepository extends CrudRepository<Winkelmandje, Long> { }
