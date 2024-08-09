package com.example.techlog.post.dto;

import java.time.LocalDate;

public record PostSimpleResponse(
        Long postId,
        String title,
        String description,
        String thumbnail,
        String writer,
        LocalDate createdAt
) {
}
