package com.myfinance.backend.movements.entities;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Entity
@Table(name = "movements")
@Data
public class UpdateAppMovements {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @PastOrPresent(message = "La fecha no puede ser futura")
    @NotNull(message = "La fecha es obligatoria")
    private LocalDate date;

    @NotBlank(message = "La descripción no puede estar vacía")
    @Size(min = 5, max = 255, message = "La descripción debe tener entre 5 y 255 caracteres")
    private String description;

    @NotNull(message = "El monto no puede ser nulo")
    @PositiveOrZero(message = "El monto debe ser mayor a cero")
    private BigDecimal amount;

    @NotBlank(message = "El tipo de movimiento no puede estar vacío")
    @Pattern(regexp = "^(income|savings|expense)$", message = "El tipo de movimiento debe ser 'income', 'savings' o 'expense'")
    private String movementType;

    @Column(name = "tag_id")
    private Long tag;
}
