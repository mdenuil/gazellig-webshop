package com.kantilever.bffwebwinkel.security.jwt.persistence;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JwtSecretRepo extends CrudRepository<JwtSecret, Long> {
    List<JwtSecret> findAll();
}
