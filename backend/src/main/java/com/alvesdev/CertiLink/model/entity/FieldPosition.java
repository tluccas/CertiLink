package com.alvesdev.CertiLink.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "field_position")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FieldPosition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "template_id", nullable = false)
    private Template template;

    @Column(nullable = false, length = 50)
    private String fieldName; // Ex: "nomeAluno"

    @Column(nullable = false)
    private Double xCoordinate;

    @Column(nullable = false)
    private Double yCoordinate;

    @Column(nullable = false)
    private Integer fontSize = 16;

    @Column(length = 10)
    private String fontColor = "#000000";
}
