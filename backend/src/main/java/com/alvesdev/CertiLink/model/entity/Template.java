package com.alvesdev.CertiLink.model.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "template")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Template {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 255)
    private String filePath; // Dir do arquivo Ex.: /uploads/templates/ (normalizar nomes antes de salvar)

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FileType fileType; // PDF ou IMAGE

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @OneToMany(mappedBy = "template", cascade = CascadeType.ALL)
    private List<FieldPosition> fields;
}