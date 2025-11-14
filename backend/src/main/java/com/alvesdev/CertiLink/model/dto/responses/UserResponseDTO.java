package com.alvesdev.CertiLink.model.dto.responses;

import com.alvesdev.CertiLink.model.entity.Users.User;

public record UserResponseDTO(Long id, String name, String email, String role) {

    public UserResponseDTO(User user){
        this(
            user.getId(),
            user.getUsername(),
            user.getEmail(),
            user.getRole().getName()
        );
    }
}
