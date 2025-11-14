package com.alvesdev.CertiLink.model.dto.responses;

import java.time.LocalDateTime;

import com.alvesdev.CertiLink.model.entity.Certificado.FileType;

public record TemplateResponseDTO (Long id, LocalDateTime created_at, FileType file_type, String name, Double x, Double y ) {
    
}
