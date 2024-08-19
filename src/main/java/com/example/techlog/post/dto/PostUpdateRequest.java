package com.example.techlog.post.dto;

import jakarta.validation.constraints.NotEmpty;

public record PostUpdateRequest(

        @NotEmpty(message = "제목은 필수 입니다.")
        String title,
        @NotEmpty(message = "본문 내용은 필수 입니다.")
        String content,
        String thumbnail
) {
}
