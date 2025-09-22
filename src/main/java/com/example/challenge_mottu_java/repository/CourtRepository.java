package com.example.challenge_mottu_java.repository;

import com.example.challenge_mottu_java.model.Court;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CourtRepository extends JpaRepository<Court, Long> {

    Optional<Court> findByNameIgnoreCase(String name);

    Optional<Court> findByAcessCode(String acessCode);
}
