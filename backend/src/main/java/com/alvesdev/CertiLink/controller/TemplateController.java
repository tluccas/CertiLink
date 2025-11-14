package com.alvesdev.CertiLink.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alvesdev.CertiLink.repository.FieldPositionRepository;
import com.alvesdev.CertiLink.repository.TemplateRepository;
import org.springframework.web.bind.annotation.GetMapping;

import com.alvesdev.CertiLink.model.dto.responses.TemplateResponseDTO;
import com.alvesdev.CertiLink.model.entity.Certificado.FieldPosition;
import com.alvesdev.CertiLink.model.entity.Certificado.Template;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("templates")
public class TemplateController {

    @Autowired
    private TemplateRepository templateRepository;

    @Autowired
    private FieldPositionRepository fieldPositionRepository;

    @GetMapping("all")
    public ResponseEntity<List<TemplateResponseDTO>> getAllTemplates() {
        List<Template> templates = templateRepository.findAll();
        List<TemplateResponseDTO> response = new ArrayList<>();
        for (Template template : templates) {
            Optional<FieldPosition> fieldPosOpt = fieldPositionRepository.findByTemplateId(template.getId());

            TemplateResponseDTO dto = new TemplateResponseDTO(
                    template.getId(),
                    template.getCreatedAt(),
                    template.getFileType(),
                    template.getName(),
                    fieldPosOpt.map(FieldPosition::getXCoordinate).orElse(null),
                    fieldPosOpt.map(FieldPosition::getYCoordinate).orElse(null));

                    response.add(dto);
          
        }

        return ResponseEntity.ok(response);

    }

}
