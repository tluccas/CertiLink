package com.alvesdev.CertiLink.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.alvesdev.CertiLink.model.entity.Users.*;

public interface RoleRepository extends JpaRepository<Role, Long> {
    
}
