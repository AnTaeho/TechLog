package com.example.techlog.post.dto;

public record PostUpdateRequest(
        String title,
        String description,
        String content
) {
}
