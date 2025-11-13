package com.alvesdev.CertiLink.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.alvesdev.CertiLink.model.entity.Users.*;

public interface UserRepository extends JpaRepository<User, Long> {
    
}
