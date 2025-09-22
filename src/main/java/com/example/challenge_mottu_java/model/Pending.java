package com.example.challenge_mottu_java.model;

import com.example.challenge_mottu_java.Enums.StatusPending;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Pending {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "O número da pendência é obrigatório")
    @Min(value = 1, message = "O número da pendência deve ser no mínimo 0")
    @Max(value = 3, message = "O número da pendência deve ser no máximo 3")
    private Long number;

    @Size(max = 400, message = "A descrição deve ter no máximo 400 caracteres")
    private String description;

    @NotNull(message = "O status da pendência é obrigatório")
    private StatusPending status;

    @ManyToOne(optional = false)
    @JoinColumn(name = "bike_id", referencedColumnName = "placa")
    private Bike bike;
}
