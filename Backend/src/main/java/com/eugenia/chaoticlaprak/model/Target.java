package com.eugenia.chaoticlaprak.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "targets")
public class Target {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String role; // dosen atau aslab

    @Column(nullable = false)
    private String spriteKey; // nama sprite di LibGDX nanti
}