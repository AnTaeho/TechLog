package com.example.techlog.user;

import com.example.techlog.common.dto.CommonResponse;
import com.example.techlog.common.dto.EmptyDto;
import com.example.techlog.common.dto.TokenResponse;
import com.example.techlog.auth.service.JwtService;
import com.example.techlog.user.dto.JoinRequest;
import com.example.techlog.user.dto.LoginRequest;
import com.example.techlog.user.dto.UserEmailVO;
import com.example.techlog.user.dto.UserIdResponse;
import com.example.techlog.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final JwtService jwtService;

    @PostMapping("/join")
    public CommonResponse<UserIdResponse> join(@RequestBody JoinRequest joinRequest) {
        return new CommonResponse<>(userService.join(joinRequest));
    }

    @PostMapping("/login")
    public CommonResponse<TokenResponse> login(@RequestBody LoginRequest loginRequest) {
        UserEmailVO email = userService.login(loginRequest);
        return new CommonResponse<>(jwtService.toTokenResponse(email.value()));
    }

    @PostMapping("/logout")
    public CommonResponse<EmptyDto> logout() {
        SecurityContextHolder.clearContext();
        return new CommonResponse<>(new EmptyDto());
    }

}
