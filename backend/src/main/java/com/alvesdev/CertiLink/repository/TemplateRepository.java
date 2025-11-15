package com.alvesdev.CertiLink.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.alvesdev.CertiLink.model.entity.Certificado.*;
public interface TemplateRepository extends JpaRepository<Template, Long> {
	boolean existsBySlug(String slug);
	Optional<Template> findBySlug(String slug);
}
