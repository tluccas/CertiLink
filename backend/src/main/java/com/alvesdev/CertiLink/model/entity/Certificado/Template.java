package com.alvesdev.CertiLink.model.entity.Certificado;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

import com.alvesdev.CertiLink.model.entity.Users.User;

@Entity
@Table(name = "template")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Template {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 150)
    private String slug; 

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 255)
    private String filePath; // Dir do arquivo Ex.: /uploads/templates/ (normalizar nomes antes de salvar)

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FileType fileType; // PDF ou IMAGE (Na versão inicial apenas IMAGE)

    @Column (name = "page_width")
    private Double pageWidth;

    @Column (name = "page_height")
    private Double pageHeight;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @OneToMany(mappedBy = "template", cascade = CascadeType.ALL)
    private List<FieldPosition> fields;
}