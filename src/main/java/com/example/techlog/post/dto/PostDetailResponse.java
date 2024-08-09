package com.example.techlog.post.dto;

import java.time.LocalDate;

public record PostDetailResponse(
        Long postId,
        String title,
        String content,
        String thumbnail,
        String writer,
        Long writerId,
        LocalDate createdAt
) {
}
