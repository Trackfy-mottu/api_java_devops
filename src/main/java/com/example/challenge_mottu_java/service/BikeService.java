package com.example.challenge_mottu_java.service;

import com.example.challenge_mottu_java.dto.BikeDTO;
import com.example.challenge_mottu_java.dto.PendingDTO;
import com.example.challenge_mottu_java.model.Bike;
import com.example.challenge_mottu_java.model.Court;
import com.example.challenge_mottu_java.repository.BikeRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BikeService {

    private final BikeRepository bikeRepository;
    private final CourtService courtService;

    public BikeService(BikeRepository bikeRepository, CourtService courtService) {
        this.bikeRepository = bikeRepository;
        this.courtService = courtService;
    }

    public BikeDTO getBikeByPlaca(String placa){
        Bike bike = bikeRepository.findById(placa)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Moto não encontrada"));
        return toDTO(bike);
    }

    public List<BikeDTO> getBikeByAcessCode(String AcessCode){
        List<Bike> bikes = bikeRepository.findByCourtAcessCode(AcessCode);
        if (bikes == null) {
            return List.of();
        }
        return bikes.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public BikeDTO createBike(Bike bike){
        // Busca o court atual do banco para ter o valor correto de currentBikes
        var court = courtService.getCourtByAcessCode(bike.getCourt().getAcessCode());

        // Verifica se já atingiu a capacidade máxima
        if (court.getCurrentBikes() >= court.getMaxCapacity()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Pátio já atingiu a capacidade máxima");
        }

        bike.setCourt(court);
        bikeRepository.save(bike);

        // Incrementa o contador de bikes
        courtService.updateBikeCount(court.getAcessCode(), court.getCurrentBikes() + 1);

        return toDTO(bike);
    }

    public BikeDTO updateBike(String placa, @RequestBody @Valid Bike bike){
        bikeRepository.findById(placa)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Moto não encontrada"));
        bike.setPlaca(placa);
        bikeRepository.save(bike);
        return toDTO(bike);

    }

    public void deleteBike(String placa){
        Bike bike = bikeRepository.findById(placa)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Moto não encontrada"));

        // Decrementa o contador de bikes
        var court = bike.getCourt();
        courtService.updateBikeCount(court.getAcessCode(), Math.max(0, court.getCurrentBikes() - 1));

        bikeRepository.delete(bike);
    }

    public Bike findByPlacaAndCourt(String placa, Court court) {
        return bikeRepository.findByPlacaAndCourt(placa, court).orElse(null);
    }

    private BikeDTO toDTO(Bike bike) {
        List<PendingDTO> pendencias = bike.getPendencias().stream()
                .map(p -> new PendingDTO(
                        p.getId(),
                        p.getNumber(),
                        p.getDescription(),
                        p.getStatus(),
                        p.getBike() != null ? p.getBike().getPlaca() : null))
                .collect(Collectors.toList());

        return new BikeDTO(
                bike.getPlaca(),
                bike.getStatus(),
                pendencias,
                bike.getModelo(),
                bike.getCourt().courtToDTO(),
                bike.getLocalizacao());
    }
}