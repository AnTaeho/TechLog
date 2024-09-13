package com.example.techlog.event.redis;

import com.example.techlog.event.refreshtoken.RefreshTokenEvent;
import com.example.techlog.event.refreshtoken.RefreshTokenEventHandler;
import com.example.techlog.refreshtoken.RefreshToken;
import com.example.techlog.refreshtoken.RefreshTokenRepository;
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
        RefreshTokenEvent refreshTokenEvent = new RefreshTokenEvent(email, refreshToken);

        // when
        refreshTokenEventHandler.saveRefreshToken(refreshTokenEvent);

        // then
        ArgumentCaptor<RefreshToken> captor = ArgumentCaptor.forClass(RefreshToken.class);
        verify(refreshTokenRepository, times(1)).save(captor.capture());
        RefreshToken capturedToken = captor.getValue();
        assertEquals(email, capturedToken.getEmail());
        assertEquals(refreshToken, capturedToken.getToken());
    }
}
