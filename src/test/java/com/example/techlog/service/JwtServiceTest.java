package com.example.techlog.service;

import com.example.techlog.auth.service.JwtService;
import com.example.techlog.common.dto.TokenResponse;
import com.example.techlog.user.domain.User;
import com.example.techlog.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
@Transactional
class JwtServiceTest {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        User user = new User("email", "password123", "John Doe");
        userRepository.save(user);
    }

    @Test
    void testToTokenResponse() {
        // When
        TokenResponse tokenResponse = jwtService.toTokenResponse("email");

        // Then
        assertThat(tokenResponse).isNotNull();
        assertThat(tokenResponse.accessToken()).isNotBlank();
        assertThat(tokenResponse.refreshToken()).isNotBlank();
    }

    @Test
    void testExtractToken() {
        // Given
        HttpServletRequest request = mock(HttpServletRequest.class);
        String token = jwtService.toTokenResponse("email").accessToken();
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);

        // When
        String extractedToken = jwtService.extractToken(request);

        // Then
        assertThat(extractedToken).isEqualTo(token);
    }

    @Test
    void testGetAuthentication() {
        // Given
        String token = jwtService.toTokenResponse("email").accessToken();

        // When
        var authentication = jwtService.getAuthentication(token);

        // Then
        assertThat(authentication).isNotNull();
        assertThat(authentication.getName()).isEqualTo("email");
    }

    @Test
    void testExtractEmail() {
        // Given
        String token = jwtService.toTokenResponse("email").accessToken();

        // When
        String extractedEmail = jwtService.extractEmail(token);

        // Then
        assertThat(extractedEmail).isEqualTo("email");
    }

    @Test
    void testIsTokenValid() {
        // Given
        String validToken = jwtService.toTokenResponse("email").accessToken();
        String invalidToken = "invalidToken";

        // When
        boolean isValid = jwtService.isTokenValid(validToken);
        boolean isInvalid = jwtService.isTokenValid(invalidToken);

        // Then
        assertThat(isValid).isTrue();
        assertThat(isInvalid).isFalse();
    }
}

