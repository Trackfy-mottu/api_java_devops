package com.example.challenge_mottu_java.dto;

public record CourtDTO (
        String acessCode,
        String name,
        String address,
        Integer maxCapacity,
        Integer currentBikes
){
}
