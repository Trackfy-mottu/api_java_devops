package com.example.challenge_mottu_java.dto;

import com.example.challenge_mottu_java.Enums.RolesUser;

public record UserDTO (Long id, String username, String name, CourtDTO courtDTO, RolesUser role) {
}
