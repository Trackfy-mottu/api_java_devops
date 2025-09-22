package com.example.challenge_mottu_java.model;

import com.example.challenge_mottu_java.Enums.RolesUser;
import com.example.challenge_mottu_java.dto.CourtDTO;

public record Token (
        String token,
        String username,
        RolesUser role,
        CourtDTO court
) {
}
