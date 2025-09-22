package com.example.challenge_mottu_java.model;

import java.util.ArrayList;
import java.util.List;

import com.example.challenge_mottu_java.Enums.LocationBike;
import com.example.challenge_mottu_java.Enums.ModelsBike;
import com.example.challenge_mottu_java.Enums.StatusBike;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Bike {
    @Id
    @NotBlank(message = "O número da placa da moto não pode ser nulo")
    private String placa;

    @NotNull(message = "O número do chassi da moto não pode ser nulo")
    private Long idChassi;

    @NotNull(message = "A localizacao da moto é obrigatória")
    private LocationBike localizacao;

    @NotNull(message = "O status da moto é obrigatório")
    private StatusBike status;

    @OneToMany(mappedBy = "bike", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Pending> pendencias = new ArrayList<>();

    @NotNull(message = "O modelo da moto é obrigatório")
    private ModelsBike modelo;

    @ManyToOne(optional = false)
    @JoinColumn(name = "court_acess_code", referencedColumnName = "acess_code")
    private Court court;

}
