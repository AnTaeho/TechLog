package com.example.techlog.user.controller;

import com.example.techlog.auth.service.JwtService;
import com.example.techlog.common.dto.CommonResponse;
import com.example.techlog.common.dto.EmptyDto;
import com.example.techlog.common.dto.TokenResponse;
import com.example.techlog.user.dto.JoinRequest;
import com.example.techlog.user.dto.LoginRequest;
import com.example.techlog.user.dto.UserEmailVO;
import com.example.techlog.user.dto.UserIdResponse;
import com.example.techlog.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final JwtService jwtService;

    @PostMapping("/join")
    public CommonResponse<UserIdResponse> join(@Valid @RequestBody JoinRequest joinRequest) {
        return new CommonResponse<>(userService.join(joinRequest));
    }

    @PostMapping("/login")
    public CommonResponse<TokenResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        UserEmailVO email = userService.login(loginRequest);
        return new CommonResponse<>(jwtService.toTokenResponse(email.value()));
    }

    @PostMapping("/logout")
    public CommonResponse<EmptyDto> logout() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        userService.logout(email);
        SecurityContextHolder.clearContext();
        return CommonResponse.EMPTY;
    }

    @PostMapping("/reissue")
    public CommonResponse<TokenResponse> reIssueToken(@RequestParam("refreshToken") String refreshToken) {
        UserEmailVO email =userService.reIssueToken(refreshToken);
        return new CommonResponse<>(jwtService.toTokenResponse(email.value()));
    }

}
