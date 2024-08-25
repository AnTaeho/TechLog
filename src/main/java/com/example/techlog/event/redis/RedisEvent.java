package com.example.techlog.event.redis;

public record RedisEvent(
        String email,
        String refreshToken
) {
}
