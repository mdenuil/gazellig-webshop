package com.kantilever.bffwebwinkel.domain.bestelling.controllers;

import javax.validation.Valid;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.kantilever.bffwebwinkel.domain.bestelling.amqp.senders.BestellingEventSenderService;
import com.kantilever.bffwebwinkel.domain.bestelling.dto.BestellingDto;
import com.kantilever.bffwebwinkel.domain.bestelling.services.BestellingService;
import com.kantilever.bffwebwinkel.security.services.UserDetailsImpl;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 * BestellingController holds the endpoints for {@link com.kantilever.bffwebwinkel.domain.bestelling.models.Bestelling}
 * objects.
 *
 * All endpoints in this controller require Authenticated users.
 */
@CrossOrigin(origins = "*")
@RestController
@Log4j2
@RequestMapping("bestelling")
public class BestellingController {
    private BestellingEventSenderService bestellingEventSenderService;
    private BestellingService bestellingService;

    @Autowired
    public BestellingController(BestellingEventSenderService bestellingEventSenderService,
                                BestellingService bestellingService) {
        this.bestellingEventSenderService = bestellingEventSenderService;
        this.bestellingService = bestellingService;
    }


    /**
     * Endpoint for Posting a new {@link com.kantilever.bffwebwinkel.domain.bestelling.models.Bestelling}. This endpoint
     * requires Authentication from a registered client.
     *
     * Bestellingen posted this way automatically have the authenticated client set as owner of the Bestelling.
     *
     * @param bestellingDto payload of the bestelling.
     * @param authentication object containing credentials injected by Spring based on JWT token send by client
     * @return ResponseEntity containing posted bestelling or error message
     */
    @PostMapping
    @ResponseBody
    @PreAuthorize("hasAuthority('PARTICULIER') or hasAuthority('BEDRIJF')")
    public ResponseEntity addNewBestelling(@RequestBody @Valid BestellingDto bestellingDto,
                                           Authentication authentication) {
        try {
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            bestellingEventSenderService.sendBestellingGeplaatstEvent(bestellingDto, userDetails.getId());
            return ResponseEntity.ok(bestellingDto);
        } catch (JsonProcessingException e) {
            log.error("Error during adding new Bestelling", e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Find all {@link com.kantilever.bffwebwinkel.domain.bestelling.models.Bestelling} for a user.
     *
     * @param authentication object containing credentials injected by Spring based on JWT token send by client
     * @return ResponseEntity containing list of bestellingen. Can be an empty list.
     */
    @GetMapping
    @ResponseBody
    @PreAuthorize("hasAuthority('PARTICULIER') or hasAuthority('BEDRIJF')")
    public ResponseEntity findAllByUser(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        var allBestellingenForUser = bestellingService.findAllBestellingenForKlant(userDetails.getId());
        return ResponseEntity.ok(allBestellingenForUser);
    }
}
