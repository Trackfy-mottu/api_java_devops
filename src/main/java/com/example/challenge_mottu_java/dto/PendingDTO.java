package com.example.challenge_mottu_java.dto;

import com.example.challenge_mottu_java.Enums.StatusPending;

public record PendingDTO(
        Long id,
        Long number,
        String descricao,
        StatusPending status,
        String placaMoto) {
}
