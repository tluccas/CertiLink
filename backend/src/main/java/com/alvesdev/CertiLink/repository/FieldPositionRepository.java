package com.alvesdev.CertiLink.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.alvesdev.CertiLink.model.entity.Certificado.*;

public interface FieldPositionRepository extends JpaRepository<FieldPosition, Long> {
    Optional<FieldPosition> findByTemplateId(Long templateId);
} 
