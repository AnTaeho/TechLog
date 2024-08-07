package com.example.techlog.user.service;

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
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public UserIdResponse join(JoinRequest joinRequest) {
        User user = new User(joinRequest.email(), joinRequest.password(), joinRequest.name());
        user.encodePassword(passwordEncoder);
        User savedUser = userRepository.save(user);
        return new UserIdResponse(savedUser.getId());
    }

    @Transactional
    public UserEmailVO login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(loginRequest.toAuthenticationToken());
        return new UserEmailVO(authentication.getName());
    }

}
