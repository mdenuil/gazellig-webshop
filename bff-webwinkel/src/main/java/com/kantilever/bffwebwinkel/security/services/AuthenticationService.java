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
import com.kantilever.bffwebwinkel.security.payload.response.JwtResponse;
import com.kantilever.bffwebwinkel.security.payload.response.MessageResponse;
import java.util.HashSet;
import java.util.Set;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * AuthenticationService is responsible for processing Authentication requests from the client.
 * <p>
 * This class does not persist any Klant information, and rather sends oun AMQP events about registrations.
 * {@link com.kantilever.bffwebwinkel.domain.klant.amqp.receivers.KlantToegevoegdEventReceiver} is subsequently
 * responsible for receiving Klant registration events and persisting them.
 */
@Service
@Log4j2
public class AuthenticationService {
    private AuthenticationManager authenticationManager;
    private KlantRepository klantRepository;
    private KlantSoortRepository klantSoortRepository;
    private KlantEventSenderService klantEventSenderService;
    private PasswordEncoder encoder;
    private JwtUtils jwtUtils;

    @Autowired
    public AuthenticationService(
            AuthenticationManager authenticationManager,
            KlantRepository klantRepository,
            KlantSoortRepository klantSoortRepository,
            KlantEventSenderService klantEventSenderService,
            PasswordEncoder encoder,
            JwtUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.klantRepository = klantRepository;
        this.klantSoortRepository = klantSoortRepository;
        this.klantEventSenderService = klantEventSenderService;
        this.encoder = encoder;
        this.jwtUtils = jwtUtils;
    }

    /**
     * Authenticate a user against the SecurityContext. If a valid user is found with the send credentials a JWT token
     * response is send back.
     *
     * @param inlogRequest DTO body for login.
     * @return ResponseEntity containing JWT token of login
     */
    public ResponseEntity<JwtResponse> authenticateUser(InlogRequest inlogRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(inlogRequest.getEmail(), inlogRequest.getWachtwoord()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        return ResponseEntity.ok(new JwtResponse(jwt));
    }

    /**
     * Build a {@link Klant} object from a {@link RegistreerRequest} and sends a
     * {@link com.kantilever.bffwebwinkel.domain.klant.amqp.events.KlantEvent}
     *
     * @param registreerRequest DTO body of registreer POST request
     * @return ResponseEntity containing message if registreer request was successful
     */
    public ResponseEntity<MessageResponse> registerUser(RegistreerRequest registreerRequest) {
        if (klantRepository.existsByEmail(registreerRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(MessageResponse.of("Error: Email is already in use!"));
        }

        Klant klant = buildKlantFromRegistreerRequest(registreerRequest);

        try {
            klantEventSenderService.sendKlantGeregistreerdEvent(klant);
        } catch (JsonProcessingException e) {
            log.error("User could not be created", e);
            return ResponseEntity.badRequest().body(MessageResponse.of("Error: Could not create User"));
        }

        return ResponseEntity.ok(MessageResponse.of("User registered successfully!"));
    }

    private Klant buildKlantFromRegistreerRequest(RegistreerRequest registreerRequest) {
        Klant klant = Klant.builder()
                .email(registreerRequest.getEmail())
                .wachtwoord(encoder.encode(registreerRequest.getWachtwoord()))
                .initialen(registreerRequest.getInitialen())
                .achternaam(registreerRequest.getAchternaam())
                .build();

        Set<KlantSoort> klantSoorten = new HashSet<>();

        registreerRequest.getKlantSoort().forEach(klantSoort -> {
            switch (klantSoort) {
                case "bedrijf":
                    KlantSoort bedrijf = klantSoortRepository.findByNaam(EKlantSoort.BEDRIJF)
                            .orElseThrow(() -> new RuntimeException("Error: Customer type is not found."));
                    klantSoorten.add(bedrijf);

                    break;
                case "particulier":
                    KlantSoort particulier = klantSoortRepository.findByNaam(EKlantSoort.PARTICULIER)
                            .orElseThrow(() -> new RuntimeException("Error: Customer type is not found."));
                    klantSoorten.add(particulier);

                    break;
                default:
                    break;
            }
        });

        klant.setKlantSoort(klantSoorten);
        log.info(String.format("Registered new Klant: %s", klant));

        return klant;
    }
}
