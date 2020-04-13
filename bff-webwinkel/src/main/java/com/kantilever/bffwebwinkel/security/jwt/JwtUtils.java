package com.kantilever.bffwebwinkel.security.jwt;

import com.kantilever.bffwebwinkel.security.jwt.persistence.JwtSecret;
import com.kantilever.bffwebwinkel.security.jwt.persistence.JwtSecretRepo;
import com.kantilever.bffwebwinkel.security.services.UserDetailsImpl;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * JwtUtils provides helper functions for creating and validating JWT tokens.
 * <p>
 * This class generates a secret and persists it to the database. On subsequent startups the secret from the database
 * is used.
 */
@Log4j2
@Component
@Transactional
public class JwtUtils {
    @Value("${kantilever.app.jwtExpirationMs}")
    private int jwtExpirationMs;
    private JwtSecretRepo jwtSecretRepo;
    private SecretKey jwtSecretKey;

    @Autowired
    public JwtUtils(JwtSecretRepo jwtSecretRepo) {
        this.jwtSecretRepo = jwtSecretRepo;
    }

    @PostConstruct
    void buildOrRetrieveSecretKey() {
        log.info("Initializing JWT secret");
        List<JwtSecret> jwtSecrets = jwtSecretRepo.findAll();

        if (!jwtSecrets.isEmpty()) {
            this.jwtSecretKey = loadExistingKey(jwtSecrets.get(0));
        } else {
            this.jwtSecretKey = buildAndPersistNewSecret();
        }
    }

    /**
     * Generate a valid JWT Token from an Authentication object.
     * <p>
     * The secret used to sign the JWT token with is randomly generated on boot and persisted and loaded on subsequent
     * boots.
     *
     * @param authentication Authentication object to generate JWT token for
     * @return Base64 encoded JWT token
     */
    public String generateJwtToken(Authentication authentication) {

        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setId(String.valueOf(userPrincipal.getId()))
                .setSubject((userPrincipal.getUsername()))
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .claim("roles", userPrincipal.getAuthorities())
                .signWith(jwtSecretKey, SignatureAlgorithm.HS512)
                .compact();
    }

    String getUserNameFromJwtToken(String token) {
        return Jwts.parser().setSigningKey(jwtSecretKey).parseClaimsJws(token).getBody().getSubject();
    }

    boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecretKey).parseClaimsJws(authToken);
            return true;
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token", e);
        } catch (ExpiredJwtException e) {
            log.error("JWT token is expired", e);
        } catch (UnsupportedJwtException e) {
            log.error("JWT token is unsupported", e);
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty", e);
        }

        return false;
    }

    private SecretKey buildAndPersistNewSecret() {
        log.info("Building new secret");
        var key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
        var jwtSecret = JwtSecret.of(Base64.getEncoder().encodeToString(key.getEncoded()));
        jwtSecretRepo.save(jwtSecret);
        return key;
    }

    private SecretKey loadExistingKey(JwtSecret jwtSecret) {
        log.info("Using existing secret");
        byte[] decodedKey = Base64.getDecoder().decode(jwtSecret.getKey());
        return new SecretKeySpec(decodedKey, 0, decodedKey.length, "HmacSHA512");
    }
}
