package com.example.techlog.event.refreshtoken;

public record RefreshTokenEvent(
        String email,
        String refreshToken
) {
}
