package com.myfinance.backend.movements.entities;

import lombok.Data;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "tags")
@Data
public class AppTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "La descripción no puede estar vacía")
    @Size(max = 25, message = "El nombre no es valido")
    private String name;

    private Boolean isGlobal;

    @Column(name = "user_id", nullable = true)
    private Long userId;

}
