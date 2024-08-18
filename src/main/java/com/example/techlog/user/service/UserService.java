package com.example.techlog.user.service;

import com.example.techlog.redis.RefreshToken;
import com.example.techlog.redis.RefreshTokenRepository;
import com.example.techlog.user.domain.User;
import com.example.techlog.user.dto.JoinRequest;
import com.example.techlog.user.dto.LoginRequest;
import com.example.techlog.user.dto.UserEmailVO;
import com.example.techlog.user.dto.UserIdResponse;
import com.example.techlog.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public UserIdResponse join(JoinRequest joinRequest) {
        checkEmail(joinRequest);
        User user = new User(joinRequest.email(), joinRequest.password(), joinRequest.name());
        user.encodePassword(passwordEncoder);
        User savedUser = userRepository.save(user);
        return new UserIdResponse(savedUser.getId());
    }

    private void checkEmail(JoinRequest joinRequest) {
        if (userRepository.existsByEmail(joinRequest.email())) {
            throw new IllegalArgumentException("해당 이메일은 이미 존재합니다.");
        }
    }

    @Transactional
    public UserEmailVO login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(loginRequest.toAuthenticationToken());
        return new UserEmailVO(authentication.getName());
    }

    @Transactional
    public UserEmailVO reIssueToken(String refreshToken) {
        RefreshToken byToken = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new IllegalArgumentException("그런 토큰은 없는디요."));
        refreshTokenRepository.delete(byToken);
        return new UserEmailVO(byToken.getEmail());
    }

    @Transactional
    public void logout(String email) {
        System.out.println(email);
        refreshTokenRepository.deleteById(email);
    }
}
