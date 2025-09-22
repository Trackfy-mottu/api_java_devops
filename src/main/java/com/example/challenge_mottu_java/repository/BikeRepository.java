package com.example.challenge_mottu_java.repository;

import com.example.challenge_mottu_java.model.Court;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.challenge_mottu_java.model.Bike;

import java.util.List;
import java.util.Optional;

public interface BikeRepository extends JpaRepository<Bike, String> {
    List<Bike> findByCourtAcessCode(String AcessCode);

    Optional<Bike> findByPlacaAndCourt(String placa, Court court);

}
