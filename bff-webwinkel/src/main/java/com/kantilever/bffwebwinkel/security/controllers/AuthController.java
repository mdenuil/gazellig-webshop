package com.kantilever.bffwebwinkel.security.controllers;

import com.kantilever.bffwebwinkel.security.payload.request.InlogRequest;
import com.kantilever.bffwebwinkel.security.payload.request.RegistreerRequest;
import com.kantilever.bffwebwinkel.security.payload.response.JwtResponse;
import com.kantilever.bffwebwinkel.security.payload.response.MessageResponse;
import com.kantilever.bffwebwinkel.security.services.AuthenticationService;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * AuthController holds the endpoints for Klant Authentication. It opens up a Login and Registeer endpoint.
 */
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/auth")
public class AuthController {
    private AuthenticationService authenticationService;

    @Autowired
    public AuthController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    /**
     * Make a request for a JWT token based on username and password. If it's a known combination a JWT token is
     * produced and returned
     *
     * @param inlogRequest contains the username and password fields
     * @return JWT token
     */
    @PostMapping("/login")
    public ResponseEntity<JwtResponse> authenticateUser(@Valid @RequestBody InlogRequest inlogRequest) {
        return authenticationService.authenticateUser(inlogRequest);
    }

    /**
     * Make a request to persist a new user with user details and credentials.
     *
     * @param signUpRequest contains user details and credentials
     * @return Success or failure response.
     */
    @PostMapping("/registreer")
    public ResponseEntity<MessageResponse> registerUser(@Valid @RequestBody RegistreerRequest signUpRequest) {
        return authenticationService.registerUser(signUpRequest);
    }
}
