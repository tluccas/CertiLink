package com.alvesdev.CertiLink.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alvesdev.CertiLink.model.entity.Users.User;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/users")
public class UserController {


    @GetMapping("path")
    public ResponseEntity<User> getAllUsers(@RequestParam String param) {
        return ResponseEntity.ok(new User());
    }
    
    
}
