package com.example.techlog.event.redis;

import com.example.techlog.redis.RefreshToken;
import com.example.techlog.redis.RefreshTokenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class RefreshTokenEventHandlerTest {

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @InjectMocks
    private RefreshTokenEventHandler refreshTokenEventHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveRefreshToken_shouldSaveTokenToRepository() {
        // given
        String email = "test@example.com";
        String refreshToken = "testRefreshToken";
        RedisEvent redisEvent = new RedisEvent(email, refreshToken);

        // when
        refreshTokenEventHandler.saveRefreshToken(redisEvent);

        // then
        ArgumentCaptor<RefreshToken> captor = ArgumentCaptor.forClass(RefreshToken.class);
        verify(refreshTokenRepository, times(1)).save(captor.capture());
        RefreshToken capturedToken = captor.getValue();
        assertEquals(email, capturedToken.getEmail());
        assertEquals(refreshToken, capturedToken.getToken());
    }
}
