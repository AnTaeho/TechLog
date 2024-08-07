package com.example.techlog.common;

public record TokenResponse(
        String accessToken,
        String refreshToken
) {
}
