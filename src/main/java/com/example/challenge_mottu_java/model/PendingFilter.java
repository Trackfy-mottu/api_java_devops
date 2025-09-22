package com.example.challenge_mottu_java.model;

import com.example.challenge_mottu_java.Enums.StatusPending;

public record PendingFilter(String description,
Long number, StatusPending status, String placaDaMoto) {

}
