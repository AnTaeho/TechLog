package com.example.techlog.common.dto;

public record TokenResponse(
        String accessToken,
        String refreshToken,
        String name
) {
}
