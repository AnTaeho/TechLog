package com.example.techlog.event.redis;

import com.example.techlog.redis.RefreshToken;
import com.example.techlog.redis.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RefreshTokenEventHandler {

    private final RefreshTokenRepository refreshTokenRepository;

    @Async
    @EventListener(RedisEvent.class)
    public void saveRefreshToken(RedisEvent event) {
        refreshTokenRepository.save(new RefreshToken(event.email(), event.refreshToken()));
    }
}
