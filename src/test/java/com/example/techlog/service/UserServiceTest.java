package com.example.techlog.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.example.techlog.refreshtoken.RefreshToken;
import com.example.techlog.refreshtoken.RefreshTokenRepository;
import com.example.techlog.user.domain.User;
import com.example.techlog.user.dto.JoinRequest;
import com.example.techlog.user.dto.LoginRequest;
import com.example.techlog.user.dto.UserEmailVO;
import com.example.techlog.user.dto.UserIdResponse;
import com.example.techlog.user.repository.UserRepository;
import com.example.techlog.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    private User existingUser;

    @BeforeEach
    void setUp() {
        // given
        existingUser = new User("existing@example.com", "validpassword", "Existing User");
        existingUser.encodePassword(passwordEncoder);
        userRepository.save(existingUser);
    }

    @Test
    @DisplayName("회원 가입 테스트")
    void joinWithValidDataTest() {
        // given
        JoinRequest joinRequest = new JoinRequest("newuser@example.com", "newpassword", "New User");

        // when
        UserIdResponse response = userService.join(joinRequest);

        // then
        assertThat(response).isNotNull();
        assertThat(userRepository.findById(response.userId())).isPresent();
    }

    @Test
    @DisplayName("중복되는 이메일로는 가입 할 수 없습니다.")
    void joinWithDuplicateEmailTest() {
        // given
        JoinRequest joinRequest = new JoinRequest("existing@example.com", "anotherpassword", "Another User");

        // when // then
        assertThrows(IllegalArgumentException.class, () -> userService.join(joinRequest));
    }

    @Test
    @DisplayName("로그인 테스트")
    void loginWithValidDataTest() {
        // given
        LoginRequest loginRequest = new LoginRequest("existing@example.com", "validpassword");

        // when
        UserEmailVO userEmailVO = userService.login(loginRequest);

        // then
        assertThat(userEmailVO.value()).isEqualTo("existing@example.com");
    }

    @Test
    @DisplayName("잘못된 비밀번호로 로그인 하면 실패합니다.")
    void loginWithInvalidCredentialsTest() {
        // given
        LoginRequest loginRequest = new LoginRequest("existing@example.com", "wrongpassword");

        // when // then
        assertThrows(Exception.class, () -> userService.login(loginRequest));
    }

    @Test
    @DisplayName("토큰 재발급 테스트")
    void reIssueTokenWithValidTokenTest() {
        // given
        RefreshToken refreshToken = new RefreshToken(existingUser.getEmail(), "valid-refresh-token");
        refreshTokenRepository.save(refreshToken);

        // when
        UserEmailVO userEmailVO = userService.reIssueToken("valid-refresh-token");

        // then
        assertThat(userEmailVO.value()).isEqualTo(existingUser.getEmail());
        assertThat(refreshTokenRepository.findByToken("valid-refresh-token")).isNotPresent();
    }

    @Test
    @DisplayName("잘못된 토큰으로 재발급 요청하면 실패합니다.")
    void reIssueTokenWithInvalidTokenTest() {
        // when // then
        assertThrows(IllegalArgumentException.class, () -> userService.reIssueToken("invalid-refresh-token"));
    }

    @Test
    @DisplayName("로그아웃 테스트")
    void logoutTest() {
        // given
        RefreshToken refreshToken = new RefreshToken(existingUser.getEmail(), "valid-refresh-token");
        refreshTokenRepository.save(refreshToken);

        // when
        userService.logout(existingUser.getEmail());

        // then
        assertThat(refreshTokenRepository.findById(existingUser.getEmail())).isNotPresent();
    }
}
