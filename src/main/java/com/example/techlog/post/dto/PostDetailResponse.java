package com.example.techlog.post.dto;

import java.time.LocalDate;

public record PostDetailResponse(
        String title,
        String content,
        String writer,
        Long writerId,
        LocalDate createdAt
) {
}
