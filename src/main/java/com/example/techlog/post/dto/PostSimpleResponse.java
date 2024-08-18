package com.example.techlog.post.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

public record PostSimpleResponse(
        Long postId,
        String title,
        String thumbnail,
        String writer,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        String createdAt
) {
}
