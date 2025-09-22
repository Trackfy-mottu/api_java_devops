package com.example.challenge_mottu_java.controller.api;

import com.example.challenge_mottu_java.dto.PendingDTO;
import com.example.challenge_mottu_java.model.Pending;
import com.example.challenge_mottu_java.service.PendingService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/pendings")
public class PendingController {

    Logger log = LoggerFactory.getLogger(PendingController.class);
    private final PendingService pendingService;

    public PendingController(PendingService pendingService) {
        this.pendingService = pendingService;
    }

    @GetMapping
    public ResponseEntity<Page<PendingDTO>> getAllPendings(Pageable pageable) {
        return ResponseEntity.ok(pendingService.getAllPendings(pageable));
    }

    @PostMapping
    @CacheEvict(value = "pendings", allEntries = true)
    @Operation(summary = "Cadastrar pendência", description = "Cria uma nova pendência", responses = {
            @ApiResponse(responseCode = "201"),
            @ApiResponse(responseCode = "400"),
    })
    public ResponseEntity<PendingDTO> create(@RequestBody Pending pending) {
        try {
            log.info("Criando nova pendência...");
            PendingDTO pendingDTO = pendingService.createPending(pending);
            return ResponseEntity.status(HttpStatus.CREATED).body(pendingDTO);
        } catch (RuntimeException e){
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }


    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CacheEvict(value = "pendings", allEntries = true)
    public ResponseEntity<Void> destroy(@PathVariable Long id) {
        try {
            log.info("Deletando pendência {}", id);
            pendingService.deletePending(id);
            return ResponseEntity.noContent().build();
        }catch (RuntimeException e){
            log.error(e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    @CacheEvict(value = "pendings", allEntries = true)
    public ResponseEntity<PendingDTO> update(@PathVariable Long id, @RequestBody Pending pending) {
        try {
            log.info("Atualizando pendência {}", id);
            PendingDTO pendingDTO = pendingService.updatePending(id, pending);
            return ResponseEntity.ok(pendingDTO);
        }catch (RuntimeException e){
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

}
