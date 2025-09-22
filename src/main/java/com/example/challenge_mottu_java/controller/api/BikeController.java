package com.example.challenge_mottu_java.controller.api;

import com.example.challenge_mottu_java.dto.BikeDTO;
import com.example.challenge_mottu_java.model.Bike;

import com.example.challenge_mottu_java.service.BikeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import jakarta.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("api/bike")
public class BikeController {

    private Logger log = LoggerFactory.getLogger(getClass());
    private final BikeService bikeService;

    public BikeController(BikeService bikeService) {
        this.bikeService = bikeService;
    }

    @GetMapping("/{placa}")
    public ResponseEntity<BikeDTO> getBikeByPlaca(@PathVariable String placa) {
        try {
            log.info("Buscando moto com placa: {}", placa);
            BikeDTO bikeDTO = bikeService.getBikeByPlaca(placa);
            return ResponseEntity.ok(bikeDTO);
        }catch (ResponseStatusException e) {
            log.error(e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    @CacheEvict(value = "bike", allEntries = true)
    @Operation(summary = "Cadastrar moto", description = "Insere uma moto", responses = {
            @ApiResponse(responseCode = "201"),
            @ApiResponse(responseCode = "400"),
    })
    public ResponseEntity<BikeDTO> create(@RequestBody @Valid Bike bike) {
        try {
            log.info("Cadastrando moto: ", bike.getPlaca());
            BikeDTO bikeDTO = bikeService.createBike(bike);
            return ResponseEntity.status(HttpStatus.CREATED).body(bikeDTO);
        }catch (ResponseStatusException e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{placa}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> destroy(@PathVariable String placa) {
        try {
            log.info("Apagando moto {}", placa);
            bikeService.deleteBike(placa);
            return ResponseEntity.noContent().build();
        }catch (ResponseStatusException e) {
            log.error(e.getMessage());
            return ResponseEntity.noContent().build();
        }
    }

    @PutMapping("/{placa}")
    public ResponseEntity<BikeDTO> update(@PathVariable String placa, @RequestBody @Valid Bike bike) {
        try {
            log.info("Atualizando moto: {}", placa);
            BikeDTO bikeDTO = bikeService.updateBike(placa, bike);
            return ResponseEntity.ok(bikeDTO);
        }catch (ResponseStatusException e) {
            log.error(e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
}
