package com.alvesdev.CertiLink.model.dto.requests;
import com.alvesdev.CertiLink.model.entity.Users.Role;

public record CreateUserRequest(String name, String email, String password, Role role) {}