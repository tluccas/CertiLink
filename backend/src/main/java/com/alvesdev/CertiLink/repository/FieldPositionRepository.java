package com.alvesdev.CertiLink.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.alvesdev.CertiLink.model.entity.Certificado.*;

public interface FieldPositionRepository extends JpaRepository<FieldPosition, Long> {
    List<FieldPosition> findAllByTemplateId(Long templateId);
    void deleteByTemplateId(Long templateId);
} 
