package com.example.challenge_mottu_java.model;

import com.example.challenge_mottu_java.dto.CourtDTO;
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
public class Court {

    @Id
    @Column(name = "acess_code")
    @NotBlank(message = "O nome do pátio é obrigatório")
    @Size(min = 3, max = 150, message = "O código de acesso deve ter entre 3 e 150 caracteres")
    private String acessCode;

    @NotBlank(message = "O nome do pátio é obrigatório")
    @Size(min = 1, max = 100, message = "O nome do pátio deve ter entre 1 e 100 caracteres")
    @Column(unique = true)
    private String name;

    @Size(max = 255, message = "O endereço deve ter no máximo 255 caracteres")
    private String address;

    @NotNull(message = "A capacidade máxima é obrigatória")
    @Min(value = 1, message = "A capacidade máxima deve ser no mínimo 1")
    @Column(name = "max_capacity")
    private Integer maxCapacity;

    @Builder.Default
    @Column(name = "current_bikes")
    private Integer currentBikes = 0;

    public CourtDTO courtToDTO() {
        return new CourtDTO(
                this.acessCode,
                this.name,
                this.address,
                this.maxCapacity,
                this.currentBikes
        );
    }

}
