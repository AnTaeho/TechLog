package com.example.techlog.user.dto;

public record TokenResponse(
        String accessToken,
        String refreshToken
) {
}
