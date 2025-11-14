package com.alvesdev.CertiLink.controller;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import com.alvesdev.CertiLink.service.security.AuthenticationService;
import com.alvesdev.CertiLink.model.dto.TokenResponseDTO;
import com.alvesdev.CertiLink.model.dto.requests.LoginRequest;

@RestController
@RequestMapping
public class AuthController {
    private final AuthenticationService authenticationService;
    private final AuthenticationManager authenticationManager;

    public AuthController(AuthenticationService authenticationService, AuthenticationManager authenticationManager) {
        this.authenticationService = authenticationService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("auth")
    public TokenResponseDTO authenticate(Authentication authentication,
                                         @RequestBody(required = false) LoginRequest request){
        Authentication authToUse = authentication;

        if (authToUse == null || !authToUse.isAuthenticated()) {
            if (request == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Credenciais ausentes: envie Basic Auth ou JSON");
            }
            authToUse = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
            );
        }

        String token = authenticationService.authenticate(authToUse);
        return new TokenResponseDTO(token);
    }

}
