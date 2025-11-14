package com.alvesdev.CertiLink.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.alvesdev.CertiLink.model.entity.Users.*;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);
}
