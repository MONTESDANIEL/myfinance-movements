package com.myfinance.backend.movements.entities;

import jakarta.persistence.*;
import lombok.Data;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "movements")
@Data
public class ViewAppMovements {

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

    @ManyToOne(fetch = FetchType.EAGER) // Relación con etiqueta
    @NotNull(message = "Es necesario asignar una etiqueta")
    @JoinColumn(name = "tag_id", referencedColumnName = "id")
    private AppTag tag;

}
