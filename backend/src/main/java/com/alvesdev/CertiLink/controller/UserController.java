package com.alvesdev.CertiLink.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.alvesdev.CertiLink.model.entity.Users.User;
import com.alvesdev.CertiLink.model.dto.UserResponseDTO;
import com.alvesdev.CertiLink.model.dto.requests.*;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.alvesdev.CertiLink.repository.UserRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


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

    // Hashear a senha e lidar com JWT agora
    @PostMapping("/create-user")
    public ResponseEntity<UserResponseDTO> createUser(@RequestBody CreateUserRequest entity) {
       
        User user = new User();
        user.setUsername(entity.name());
        user.setEmail(entity.email());  
        user.setPassword(entity.password());
        user.setRole(entity.role());

        userRepository.save(user);

        return ResponseEntity.ok(new UserResponseDTO(user));
    }
    
    
    
}
