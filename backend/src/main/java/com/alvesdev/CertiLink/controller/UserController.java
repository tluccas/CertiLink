package com.alvesdev.CertiLink.controller;

import com.alvesdev.CertiLink.model.entity.Users.User;
import com.alvesdev.CertiLink.model.dto.UserResponseDTO;
import com.alvesdev.CertiLink.model.dto.requests.*;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import com.alvesdev.CertiLink.repository.UserRepository;


@RestController
@RequestMapping("users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

     @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/all")
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {

        List<UserResponseDTO> allUsers = userRepository.findAll().stream()
                .map(UserResponseDTO::new)
                .toList();

        return ResponseEntity.ok(allUsers);
    }

    // Hashear a senha e lidar com JWT agora
    @PostMapping("/create-user")
    public ResponseEntity<UserResponseDTO> createUser(@RequestBody CreateUserRequest entity) {
       
        User user = new User();
        user.setUsername(entity.name());
        user.setEmail(entity.email());  
        user.setPassword(passwordEncoder.encode(entity.password()));
        user.setRole(entity.role()); // 

        userRepository.save(user);

        return ResponseEntity.ok(new UserResponseDTO(user));
    }
    
    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> me(Authentication authentication){
        String principal = authentication.getName();
        return userRepository.findByUsername(principal)
            .or(() -> userRepository.findByEmail(principal))
            .map(user -> ResponseEntity.ok(new UserResponseDTO(user)))
            .orElse(ResponseEntity.notFound().build());
    }
    
    
    
}
