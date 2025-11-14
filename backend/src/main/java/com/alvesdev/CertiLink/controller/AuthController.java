package com.alvesdev.CertiLink.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.alvesdev.CertiLink.service.security.AuthenticationService;

@RestController
@RequestMapping
public class AuthController {
    private final AuthenticationService authenticationService;


    public AuthController(AuthenticationService authenticationService, PasswordEncoder passwordEncoder) {
        this.authenticationService = authenticationService;
    } 

    @PostMapping("auth")
    public String authenticate(Authentication authentication){
        return authenticationService.authenticate(authentication);
    }

}
