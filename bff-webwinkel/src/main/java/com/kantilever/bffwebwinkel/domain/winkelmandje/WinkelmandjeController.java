package com.kantilever.bffwebwinkel.domain.winkelmandje;

import com.kantilever.bffwebwinkel.domain.winkelmandje.amqp.events.WinkelmandjeEvent;
import com.kantilever.bffwebwinkel.domain.winkelmandje.dto.WinkelmandjeDto;
import com.kantilever.bffwebwinkel.domain.winkelmandje.models.Winkelmandje;
import com.kantilever.bffwebwinkel.domain.winkelmandje.services.WinkelmandjeEventService;
import com.kantilever.bffwebwinkel.domain.winkelmandje.services.WinkelmandjeService;
import com.kantilever.bffwebwinkel.security.services.UserDetailsImpl;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Log4j2
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("winkelmandje")
public class WinkelmandjeController {

    private WinkelmandjeService winkelmandjeService;
    private WinkelmandjeEventService winkelmandjeEventService;

    @Autowired
    public WinkelmandjeController(WinkelmandjeService winkelmandjeService,
                                  WinkelmandjeEventService winkelmandjeEventService) {
        this.winkelmandjeService = winkelmandjeService;
        this.winkelmandjeEventService = winkelmandjeEventService;
    }

    @PostMapping
    @ResponseBody
    @PreAuthorize("hasAuthority('PARTICULIER') or hasAuthority('BEDRIJF')")
    public ResponseEntity<WinkelmandjeEvent> sendWinkelmandjeEvent(@RequestBody WinkelmandjeDto artikelen,
                                                                   Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        var winkelmandjeEvent = WinkelmandjeEvent.from(artikelen, userDetails.getId());
        winkelmandjeEventService.sendWinkelmandjeAangepastEvent(winkelmandjeEvent);

        return ResponseEntity.ok(winkelmandjeEvent);
    }

    @GetMapping
    @ResponseBody
    @PreAuthorize("hasAuthority('PARTICULIER') or hasAuthority('BEDRIJF')")
    public Winkelmandje findWinkelmandje(Authentication authentication) {
        var userDetails = (UserDetailsImpl) authentication.getPrincipal();

        return winkelmandjeService.getWinkelmandjeById(userDetails.getId());
    }
}
