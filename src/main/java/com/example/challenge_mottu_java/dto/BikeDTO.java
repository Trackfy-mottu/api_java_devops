package com.example.challenge_mottu_java.dto;

import java.util.List;

import com.example.challenge_mottu_java.Enums.LocationBike;
import com.example.challenge_mottu_java.Enums.ModelsBike;
import com.example.challenge_mottu_java.Enums.StatusBike;

public record BikeDTO(
        String placa,
        StatusBike status,
        List<PendingDTO> pendencias,
        ModelsBike modelo,
        CourtDTO court,
        LocationBike localizacao) {

}
