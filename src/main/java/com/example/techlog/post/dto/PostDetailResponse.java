package com.example.techlog.post.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

public record PostDetailResponse(
        Long postId,
        String title,
        String content,
        String thumbnail,
        String writer,
        Long writerId,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        String createdAt
) {
}
