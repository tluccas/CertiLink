package com.alvesdev.CertiLink.controller;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.alvesdev.CertiLink.model.dto.requests.FieldPositionRequest;
import com.alvesdev.CertiLink.model.dto.responses.FieldPositionsResponse;
import com.alvesdev.CertiLink.model.dto.responses.TemplateResponseDTO;
import com.alvesdev.CertiLink.model.dto.responses.UploadResponse;
import com.alvesdev.CertiLink.model.entity.Certificado.FieldPosition;
import com.alvesdev.CertiLink.model.entity.Certificado.FileType;
import com.alvesdev.CertiLink.model.entity.Certificado.Template;
import com.alvesdev.CertiLink.model.entity.Users.User;
import com.alvesdev.CertiLink.repository.FieldPositionRepository;
import com.alvesdev.CertiLink.repository.TemplateRepository;
import com.alvesdev.CertiLink.repository.UserRepository;
import com.alvesdev.CertiLink.service.TemplateStorageService;
import com.alvesdev.CertiLink.service.TemplateStorageService.StoredFileMeta;
import com.alvesdev.CertiLink.util.SlugUtil;

@RestController
@RequestMapping("templates")
public class TemplateController {

    private final TemplateStorageService templateStorageService;

    @Autowired
    private TemplateRepository templateRepository;

    @Autowired
    private FieldPositionRepository fieldPositionRepository;

    @Autowired
    private UserRepository userRepository;

    public TemplateController(TemplateStorageService templateStorageService) {
        this.templateStorageService = templateStorageService;
    }

    @PostMapping(value = "upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UploadResponse> uploadTemplate(
        @RequestParam("name") String name,
        @RequestParam("file") MultipartFile templateFile,
        Authentication auth ) throws IOException {
        if (name == null || name.trim().length() < 3) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        String ct = templateFile.getContentType();
        if (!"image/png".equalsIgnoreCase(ct) && !"image/jpeg".equalsIgnoreCase(ct)) {
            return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).build();
        }
        if (templateFile.getSize() <= 0 || templateFile.getSize() > (5L * 1024 * 1024)) {
            return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).build();
        }

        // Salva arquivo e mede dimensões
        StoredFileMeta stored = templateStorageService.storeFile(templateFile);
        Path absolute = Paths.get("./").resolve(stored.getRelativePath()).toAbsolutePath().normalize();
        if (!Files.exists(absolute)) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        BufferedImage img = ImageIO.read(absolute.toFile());
        if (img == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        int width = img.getWidth();
        int height = img.getHeight();

        // Slug único
        String base = SlugUtil.slugify(name);
        String slug = base;
        int i = 2;
        while (templateRepository.existsBySlug(slug)) slug = base + "-" + (i++);

        // Criador
        String identity = auth != null ? auth.getName() : null;
        User createdBy = identity != null ? userRepository.findByEmail(identity).orElse(null) : null;
        if (createdBy == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        Template t = new Template();
        t.setName(name.trim());
        t.setSlug(slug);
        t.setFileType(FileType.IMAGE);
        t.setFilePath(stored.getRelativePath());
        t.setPageWidth((double) width);
        t.setPageHeight((double) height);
        t.setCreatedBy(createdBy);

        Template saved = templateRepository.save(t);

        UploadResponse body = new UploadResponse(
            saved.getId(), saved.getSlug(), saved.getName(), saved.getFileType().name(),
            saved.getFilePath(), saved.getPageWidth().intValue(), saved.getPageHeight().intValue(),
            saved.getCreatedAt()
        );
        URI location = URI.create("/templates/" + saved.getSlug());
        return ResponseEntity.created(location).body(body);
    }

    @PutMapping("{id}/fields")
    public ResponseEntity<FieldPositionsResponse> upsertFields(
        @PathVariable("id") Long templateId,
        @RequestBody List<FieldPositionRequest> fields,
        Authentication authentication
    ) {
        Optional<Template> opt = templateRepository.findById(templateId);
        if (opt.isEmpty()) return ResponseEntity.notFound().build();
        Template template = opt.get();

        String identity = authentication != null ? authentication.getName() : null;
        if (identity == null || !template.getCreatedBy().getEmail().equalsIgnoreCase(identity)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        if (fields == null || fields.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        Set<String> allowed = Set.of("NOME_ALUNO", "DATA_ALUNO");
        long validCount = fields.stream().filter(f -> f != null && allowed.contains(f.fieldName())).count();
        if (validCount < 2) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        fieldPositionRepository.deleteByTemplateId(templateId);
        List<FieldPosition> toSave = new ArrayList<>();
        for (FieldPositionRequest f : fields) {
            if (f == null || !allowed.contains(f.fieldName())) continue;
            Double x = clampPct(f.xCoordinate());
            Double y = clampPct(f.yCoordinate());
            Integer fs = (f.fontSize() == null ? 16 : Math.max(8, Math.min(72, f.fontSize())));
            String color = (f.fontColor() == null || f.fontColor().isBlank()) ? "#000000" : f.fontColor();

            FieldPosition entity = new FieldPosition();
            entity.setTemplate(template);
            entity.setFieldName(f.fieldName());
            entity.setXCoordinate(x);
            entity.setYCoordinate(y);
            entity.setFontSize(fs);
            entity.setFontColor(color);
            toSave.add(entity);
        }
        fieldPositionRepository.saveAll(toSave);

        List<FieldPositionsResponse.FieldPositionItem> items = toSave.stream()
            .map(fp -> new FieldPositionsResponse.FieldPositionItem(
                fp.getFieldName(), fp.getXCoordinate(), fp.getYCoordinate(), fp.getFontSize(), fp.getFontColor()
            ))
            .toList();
        return ResponseEntity.ok(new FieldPositionsResponse(templateId, items));
    }

    @GetMapping("{id}/fields")
    public ResponseEntity<FieldPositionsResponse> getFields(@PathVariable("id") Long templateId, Authentication authentication) {
        Optional<Template> opt = templateRepository.findById(templateId);
        if (opt.isEmpty()) return ResponseEntity.notFound().build();
        Template template = opt.get();

        String identity = authentication != null ? authentication.getName() : null;
        if (identity == null || !template.getCreatedBy().getEmail().equalsIgnoreCase(identity)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        List<FieldPositionsResponse.FieldPositionItem> items = fieldPositionRepository.findAllByTemplateId(templateId)
            .stream()
            .map(fp -> new FieldPositionsResponse.FieldPositionItem(
                fp.getFieldName(), fp.getXCoordinate(), fp.getYCoordinate(), fp.getFontSize(), fp.getFontColor()))
            .toList();

        return ResponseEntity.ok(new FieldPositionsResponse(templateId, items));
    }

    @GetMapping("{slug}")
    public ResponseEntity<?> getTemplateBySlug(@PathVariable("slug") String slug) {
        Optional<Template> opt = templateRepository.findBySlug(slug);
        if (opt.isEmpty()) return ResponseEntity.notFound().build();
        Template template = opt.get();

        List<FieldPositionsResponse.FieldPositionItem> items = fieldPositionRepository.findAllByTemplateId(template.getId())
            .stream()
            .map(fp -> new FieldPositionsResponse.FieldPositionItem(
                fp.getFieldName(), fp.getXCoordinate(), fp.getYCoordinate(), fp.getFontSize(), fp.getFontColor()))
            .toList();

        TemplateResponseDTO header = new TemplateResponseDTO(
            template.getId(), template.getCreatedAt(), template.getFileType(), template.getName(), null, null
        );

        record PublicTemplateResponse(TemplateResponseDTO template, String filePath, Integer pageWidth, Integer pageHeight, List<FieldPositionsResponse.FieldPositionItem> fields) {}
        PublicTemplateResponse body = new PublicTemplateResponse(
            header, template.getFilePath(), template.getPageWidth().intValue(), template.getPageHeight().intValue(), items
        );
        return ResponseEntity.ok(body);
    }

    @GetMapping("all")
    public ResponseEntity<List<TemplateResponseDTO>> getAllTemplates() {
        List<Template> templates = templateRepository.findAll();
        List<TemplateResponseDTO> response = new ArrayList<>();
        for (Template template : templates) {
            TemplateResponseDTO dto = new TemplateResponseDTO(
                    template.getId(),
                    template.getCreatedAt(),
                    template.getFileType(),
                    template.getName(),
                    null,
                    null);

            response.add(dto);
        }

        return ResponseEntity.ok(response);

    }

    private static Double clampPct(Double v) {
        if (v == null) return 0.0;
        if (v < 0.0) return 0.0;
        if (v > 100.0) return 100.0;
        return v;
    }

}
