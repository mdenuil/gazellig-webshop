package com.kantilever.bffwebwinkel.security.jwt;

import java.util.Base64;
import java.util.List;
import java.util.Set;
import com.kantilever.bffwebwinkel.security.jwt.persistence.JwtSecret;
import com.kantilever.bffwebwinkel.security.jwt.persistence.JwtSecretRepo;
import com.kantilever.bffwebwinkel.security.services.UserDetailsImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtUtilsTest {
    @Mock
    private Authentication authentication;
    @Mock
    private JwtSecretRepo jwtSecretRepo;

    private JwtUtils jwtUtils;

    @BeforeEach
    void init() {
        jwtUtils = new JwtUtils(jwtSecretRepo);

        when(jwtSecretRepo.findAll()).
                thenReturn(List.of(JwtSecret.of(
                        "AVERYLONGSECRETKEYDONTSTEALPLEASE" +
                        "ITSVERYIMPORTANTANDHAS" +
                        "TOBESUPERSECUREANDSAVEORSOMETHING")));
    }

    @Test
    @DisplayName("JWT Headers gets build with correct values")
    void jwtTokenHeadersBuildsCorrectly() {
        // Arrange
        when(authentication.getPrincipal())
                .thenReturn(new UserDetailsImpl(1L, "test@example.com", "password", Set.of()));
        // Act
        jwtUtils.buildOrRetrieveSecretKey();
        String jwtToken = jwtUtils.generateJwtToken(authentication);
        String[] parts = jwtToken.split("\\.");
        String payloadJson = new String(Base64.getDecoder().decode(parts[0]));
        // Assert
        assertThat(payloadJson.contains("\"typ\":\"JWT\"")).isTrue();
        assertThat(payloadJson.contains("\"alg\":\"HS512\"")).isTrue();
    }

    @Test
    @DisplayName("JWT Payload gets build with correct values")
    void jwtTokenPayloadBuildsCorrectly() {
        // Arrange
        when(authentication.getPrincipal())
                .thenReturn(new UserDetailsImpl(1L, "test@example.com", "password", Set.of()));
        // Act
        jwtUtils.buildOrRetrieveSecretKey();
        String jwtToken = jwtUtils.generateJwtToken(authentication);
        String[] parts = jwtToken.split("\\.");
        String payloadJson = new String(Base64.getDecoder().decode(parts[1]));
        // Assert
        assertThat(payloadJson.contains("\"jti\":\"1\"")).isTrue();
        assertThat(payloadJson.contains("\"sub\":\"test@example.com\"")).isTrue();
    }
}