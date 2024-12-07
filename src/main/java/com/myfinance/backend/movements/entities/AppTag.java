package com.myfinance.backend.movements.entities;

import lombok.Data;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "tags")
@Data
public class AppTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Unique identifier for each tag

    @Column(nullable = false, length = 255)
    private String name; // Name of the tag

    @Column(name = "is_global", nullable = false)
    private Boolean isGlobal = Boolean.FALSE; // Indicates if the tag is global

    @Column(name = "user_id")
    private Long userId; // User ID for custom tags (null for global tags)
}
