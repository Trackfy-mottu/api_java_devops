package com.example.challenge_mottu_java.service;

import com.example.challenge_mottu_java.dto.PendingDTO;
import com.example.challenge_mottu_java.model.Pending;
import com.example.challenge_mottu_java.repository.PendingRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


@Service
public class PendingService {

    private final PendingRepository pendingRepository;

    public PendingService(PendingRepository pendingRepository) {
        this.pendingRepository = pendingRepository;
    }

    public Page<PendingDTO> getAllPendings(Pageable pageable) {
        return pendingRepository.findAll(pageable).map(this::toDTO);
    }

    public PendingDTO createPending(Pending pending) {
        return toDTO(pendingRepository.save(pending));
    }

    public void deletePending(Long id) {
        pendingRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pendência não encontrada"));
        pendingRepository.deleteById(id);
    }

    public PendingDTO updatePending( Long id,  Pending pending) {
        Pending oldPending = pendingRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pendência não encontrada"));
        pending.setId(oldPending.getId());
        return toDTO(pendingRepository.save(pending));
    }

    private PendingDTO toDTO(Pending pending) {
        return new PendingDTO(
                pending.getId(),
                pending.getNumber(),
                pending.getDescription(),
                pending.getStatus(),
                pending.getBike() != null ? pending.getBike().getPlaca() : null);
    }
}
