package com.example.techlog.user.dto;

public record JoinRequest(
        String email,
        String password,
        String name
) {
}
