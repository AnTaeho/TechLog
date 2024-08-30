package com.example.techlog.controller;

import com.example.techlog.auth.service.JwtService;
import com.example.techlog.common.dto.CommonResponse;
import com.example.techlog.common.dto.EmptyDto;
import com.example.techlog.common.dto.TokenResponse;
import com.example.techlog.user.controller.UserController;
import com.example.techlog.user.dto.JoinRequest;
import com.example.techlog.user.dto.LoginRequest;
import com.example.techlog.user.dto.UserEmailVO;
import com.example.techlog.user.dto.UserIdResponse;
import com.example.techlog.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private UserController userController;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    @DisplayName("회원가입 테스트")
    void join() {
        // given
        JoinRequest joinRequest = new JoinRequest("test@example.com", "passwordpassword", "name");
        UserIdResponse userIdResponse = new UserIdResponse(1L);
        when(userService.join(any(JoinRequest.class))).thenReturn(userIdResponse);

        // when
        CommonResponse<UserIdResponse> response = userController.join(joinRequest);

        // then
        assertEquals(userIdResponse, response.result());
        verify(userService, times(1)).join(any(JoinRequest.class));
    }

    @Test
    @DisplayName("로그인 테스트")
    void login() {
        // given
        LoginRequest loginRequest = new LoginRequest("test@example.com", "passwordpassword");
        UserEmailVO emailVO = new UserEmailVO("test@example.com");
        TokenResponse tokenResponse = new TokenResponse("accessToken", "testRefreshToken", "name", 1L);
        when(userService.login(any(LoginRequest.class))).thenReturn(emailVO);
        when(jwtService.toTokenResponse(anyString())).thenReturn(tokenResponse);

        // when
        CommonResponse<TokenResponse> response = userController.login(loginRequest);

        // then
        assertEquals(tokenResponse, response.result());
        verify(userService, times(1)).login(any(LoginRequest.class));
        verify(jwtService, times(1)).toTokenResponse(anyString());
    }

    @Test
    @DisplayName("로그아웃 테스트")
    void logout() {
        // given
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("test@example.com");

        // when
        CommonResponse<EmptyDto> response = userController.logout();

        // then
        assertEquals(CommonResponse.EMPTY, response);
        verify(userService, times(1)).logout(anyString());
        verify(securityContext, times(1)).getAuthentication();
        verify(authentication, times(1)).getName();
    }

    @Test
    @DisplayName("토큰 재 발행 테스트")
    void reIssueToken() {
        // given
        String refreshToken = "testRefreshToken";
        UserEmailVO emailVO = new UserEmailVO("test@example.com");
        TokenResponse tokenResponse = new TokenResponse("accessToken", "testRefreshToken", "name", 1L);
        when(userService.reIssueToken(anyString())).thenReturn(emailVO);
        when(jwtService.toTokenResponse(anyString())).thenReturn(tokenResponse);

        // when
        CommonResponse<TokenResponse> response = userController.reIssueToken(refreshToken);

        // then
        assertEquals(tokenResponse, response.result());
        verify(userService, times(1)).reIssueToken(anyString());
        verify(jwtService, times(1)).toTokenResponse(anyString());
    }
}
