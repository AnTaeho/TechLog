package com.example.techlog.event;

public record RedisEvent(
        String email,
        String refreshToken
) {
}
