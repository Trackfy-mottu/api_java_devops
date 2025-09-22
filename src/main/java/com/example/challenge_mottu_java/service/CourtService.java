package com.example.challenge_mottu_java.service;

import com.example.challenge_mottu_java.dto.CourtDTO;
import com.example.challenge_mottu_java.model.Court;
import com.example.challenge_mottu_java.repository.CourtRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourtService {

    private final CourtRepository courtRepository;

    public CourtService(CourtRepository courtRepository) {
        this.courtRepository = courtRepository;
    }

    public List<CourtDTO> getAllCourts() {
        return courtRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public Court getCourtByAcessCode(String acessCode) {
        Court court = courtRepository.findByAcessCode(acessCode)
                .orElseThrow(() -> new RuntimeException("Pátio não encontrado!"));
        return court;
    }

    public CourtDTO createCourt(Court court) {
        Court existingCourt = courtRepository.findByNameIgnoreCase(court.getName()).orElse(null);
        if (existingCourt != null) throw new RuntimeException("Já existe um pátio com este nome");

        if (court.getCurrentBikes() == null) court.setCurrentBikes(0);

        Court savedCourt = courtRepository.save(court);
        return toDTO(savedCourt);
    }

    public CourtDTO updateCourt(String acessCode, Court newCourt) {
        Court oldCourt = courtRepository.findByAcessCode(acessCode)
                .orElseThrow(() -> new RuntimeException("Pátio não encontrado!"));

        newCourt.setAcessCode(oldCourt.getAcessCode());
        Court savedCourt = courtRepository.save(newCourt);
        return toDTO(savedCourt);
    }

    public void deleteCourt(String acessCode) {
        Court court = courtRepository.findByAcessCode(acessCode)
                .orElseThrow(() -> new RuntimeException("Pátio não encontrado!"));
        courtRepository.delete(court);
    }

    public CourtDTO updateBikeCount(String acessCode, Integer newBikeCount) {
        Court court = courtRepository.findByAcessCode(acessCode)
                .orElseThrow(() -> new RuntimeException("Pátio não encontrado!"));

        if (newBikeCount < 0) throw new RuntimeException("A quantidade de bikes não pode ser negativa");

        if (newBikeCount > court.getMaxCapacity()) throw new RuntimeException("A quantidade de bikes não pode exceder a capacidade máxima");

        court.setCurrentBikes(newBikeCount);
        Court savedCourt = courtRepository.save(court);
        return toDTO(savedCourt);
    }

    private CourtDTO toDTO(Court court) {
        return new CourtDTO(
                court.getAcessCode(),
                court.getName(),
                court.getAddress(),
                court.getMaxCapacity(),
                court.getCurrentBikes()
        );
    }
}