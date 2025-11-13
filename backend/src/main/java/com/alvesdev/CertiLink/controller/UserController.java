package com.alvesdev.CertiLink.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alvesdev.CertiLink.model.dto.UserResponseDTO;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.alvesdev.CertiLink.repository.UserRepository;
@RestController
@RequestMapping("users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/all")
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {

        List<UserResponseDTO> allUsers = userRepository.findAll().stream()
                .map(UserResponseDTO::new)
                .toList();

        return ResponseEntity.ok(allUsers);
    }
    
    
}
