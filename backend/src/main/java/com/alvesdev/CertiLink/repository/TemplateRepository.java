package com.alvesdev.CertiLink.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.alvesdev.CertiLink.model.entity.Certificado.*;
public interface TemplateRepository extends JpaRepository<Template, Long> {
    
    
}
