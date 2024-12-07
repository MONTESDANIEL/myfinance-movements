package com.myfinance.backend.movements.entities;

import jakarta.persistence.*;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

import java.time.LocalDate;

@Entity
@Table(name = "movements")
@Data
public class AppMovements {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "La identificación del usuario es obligatoria")
    @Column(name = "user_id")
    private Long userId;

    @PastOrPresent(message = "La fecha no puede ser futura")
    @NotNull(message = "La fecha es obligatoria")
    private LocalDate date;

    @NotBlank(message = "La descripción no puede estar vacía")
    @Size(min = 5, max = 255, message = "La descripción debe tener entre 5 y 255 caracteres")
    private String description;

    @NotNull(message = "El monto no puede ser nulo")
    @PositiveOrZero(message = "El monto debe ser cero o positivo")
    private BigDecimal amount;

    @NotBlank(message = "El tipo de movimiento no puede estar vacío")
    @Pattern(regexp = "^(income|savings|expense)$", message = "El tipo de movimiento debe ser 'income', 'savings' o 'expense'")
    private String movementType;

    @ManyToOne(fetch = FetchType.EAGER) // Carga la etiqueta junto con el movimiento
    @JoinColumn(name = "tag_id", referencedColumnName = "id") // Clave foránea
    private AppTag tag;
}
