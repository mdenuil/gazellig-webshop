package com.kantilever.bffwebwinkel.domain.klant.controllers;

import com.kantilever.bffwebwinkel.domain.klant.dto.KlantDto;
import com.kantilever.bffwebwinkel.domain.klant.services.KlantService;
import com.kantilever.bffwebwinkel.security.services.UserDetailsImpl;
import javassist.NotFoundException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@Log4j2
@RestController
@RequestMapping("klant")
public class KlantController {
    private KlantService klantService;

    @Autowired
    public KlantController(KlantService klantService) {
        this.klantService = klantService;
    }

    /**
     * Return the {@link com.kantilever.bffwebwinkel.domain.klant.models.Klant} details for an Authenticated user.
     *
     * @param authentication object containing credentials injected by Spring based on JWT token send by client
     * @return ResponseEntity containing Klant information or error message.
     */
    @GetMapping("ik")
    @ResponseBody
    @PreAuthorize("hasAuthority('PARTICULIER') or hasAuthority('BEDRIJF')")
    public ResponseEntity findAllByUser(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        try {
            var klant = klantService.findKlantById(userDetails.getId());
            return ResponseEntity.ok(KlantDto.from(klant));
        } catch (NotFoundException e) {
            log.error("Error during findAllByUser", e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
