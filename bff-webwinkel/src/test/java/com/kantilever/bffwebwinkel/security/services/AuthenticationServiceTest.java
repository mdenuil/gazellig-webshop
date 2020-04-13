package com.kantilever.bffwebwinkel.security.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kantilever.bffwebwinkel.domain.klant.amqp.sender.KlantEventSenderService;
import com.kantilever.bffwebwinkel.domain.klant.models.EKlantSoort;
import com.kantilever.bffwebwinkel.domain.klant.models.Klant;
import com.kantilever.bffwebwinkel.domain.klant.models.KlantSoort;
import com.kantilever.bffwebwinkel.domain.klant.repositories.KlantRepository;
import com.kantilever.bffwebwinkel.domain.klant.repositories.KlantSoortRepository;
import com.kantilever.bffwebwinkel.security.jwt.JwtUtils;
import com.kantilever.bffwebwinkel.security.payload.request.InlogRequest;
import com.kantilever.bffwebwinkel.security.payload.request.RegistreerRequest;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private KlantRepository klantRepository;
    @Mock
    private KlantSoortRepository klantSoortRepository;
    @Mock
    private KlantEventSenderService klantEventSenderService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtUtils jwtUtils;
    @Mock
    private SecurityContext securityContext;

    private AuthenticationService authenticationService;

    @BeforeEach
    void init() {
        this.authenticationService = new AuthenticationService(
                authenticationManager,
                klantRepository,
                klantSoortRepository,
                klantEventSenderService,
                passwordEncoder,
                jwtUtils
        );
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    @DisplayName("authenticateUser when user is known returns ResponseBody.OK with Payload JWT")
    void authenticateUser_returnsJwt() {
        // Arrange
        var inlogRequest = mock(InlogRequest.class);
        var authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(jwtUtils.generateJwtToken(authentication)).thenReturn("Bearer Token");
        // Act
        var actual = authenticationService.authenticateUser(inlogRequest);
        // Assert
        verify(securityContext, times(1)).setAuthentication(authentication);
        assertThat(actual.getBody().getToken()).isEqualTo("Bearer Token");
    }

    @Test
    @DisplayName("authenticateUser when user is unknown throws AuthenticationException")
    void authenticateUser_throwsException() {
        // Arrange
        var inlogRequest = mock(InlogRequest.class);
        var authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any())).thenThrow(AuthenticationCredentialsNotFoundException.class);
        // Act
        assertThatThrownBy(() -> authenticationService.authenticateUser(inlogRequest))
                .isInstanceOf(AuthenticationException.class);
        // Assert
        verify(securityContext, times(0)).setAuthentication(authentication);
    }

    @Test
    @DisplayName("registerUser returns a ResponseEntity.BAD_REQUEST when email is already in use")
    void registerUser_returnsBadRequest_whenEmailInUse() {
        // Arrange
        var registreerRequest = mock(RegistreerRequest.class);
        when(klantRepository.existsByEmail(any())).thenReturn(true);
        // Act
        var actual = authenticationService.registerUser(registreerRequest);
        // Assert
        assertThat(actual.getBody().getMessage()).isEqualTo("Error: Email is already in use!");
    }

    @Test
    @DisplayName("registerUser with new user builds correct RegisterEvent")
    void registerUser_buildsCorrectKlantEvent() throws JsonProcessingException {
        // Arrange
        var registreerRequest = mock(RegistreerRequest.class);
        var expectedKlant = Klant.builder()
                .klantSoort(Set.of(KlantSoort.from(EKlantSoort.PARTICULIER)))
                .email("email@example.com")
                .wachtwoord("encodedString")
                .build();
        // Mock not existing Klant
        when(klantRepository.existsByEmail(any())).thenReturn(false);
        // Mock RegistreerRequest
        when(registreerRequest.getEmail()).thenReturn("email@example.com");
        when(registreerRequest.getWachtwoord()).thenReturn("password");
        when(registreerRequest.getKlantSoort()).thenReturn(Set.of("particulier"));
        // Mock Klant object building
        when(passwordEncoder.encode(any())).thenReturn("encodedString");
        when(klantSoortRepository.findByNaam(EKlantSoort.PARTICULIER))
                .thenReturn(Optional.of(KlantSoort.from(EKlantSoort.PARTICULIER)));
        // Act
        var actual = authenticationService.registerUser(registreerRequest);
        // Assert
        verify(klantEventSenderService, times(1))
                .sendKlantGeregistreerdEvent(expectedKlant);
        assertThat(actual.getBody().getMessage()).isEqualTo("User registered successfully!");
    }
}
