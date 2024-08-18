package com.example.techlog.user.dto;

import jakarta.validation.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

public record LoginRequest(

        @Email(message = "올바른 이메일 형식이 아닙니다.")
        String email,

        @Length(min = 10, message = "10자 이상의 비밀번호를 설정해주세요.")
        String password
) {

    public Authentication toAuthenticationToken() {
        return new UsernamePasswordAuthenticationToken(email, password);
    }
}
