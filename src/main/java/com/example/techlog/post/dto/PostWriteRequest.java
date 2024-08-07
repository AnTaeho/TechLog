package com.example.techlog.post.dto;

public record PostWriteRequest(
        String title,
        String description,
        String content
) {
}
