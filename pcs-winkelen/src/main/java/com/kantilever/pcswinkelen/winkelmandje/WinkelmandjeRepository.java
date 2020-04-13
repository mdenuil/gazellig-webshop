package com.kantilever.pcswinkelen.winkelmandje;

import com.kantilever.pcswinkelen.winkelmandje.models.Winkelmandje;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WinkelmandjeRepository extends CrudRepository<Winkelmandje, Long> { }
