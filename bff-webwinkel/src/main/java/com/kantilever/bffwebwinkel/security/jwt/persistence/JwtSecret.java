package com.kantilever.bffwebwinkel.security.jwt.persistence;

import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * JwtSecret represents the Secret of that gets used for signing JwtTokens. This key is generated if not exists on
 * startup of the service. On subsequent loads this value is retrieved from the persistence layer.
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtSecret {
    @Id
    private String key;

    public static JwtSecret of(String s) {
        return new JwtSecret(s);
    }
}
