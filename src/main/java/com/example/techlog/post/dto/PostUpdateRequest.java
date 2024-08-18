package com.example.techlog.post.dto;

import jakarta.validation.constraints.NotEmpty;

public record PostUpdateRequest(

        @NotEmpty
        String title,
        @NotEmpty
        String content,
        String thumbnail
) {
}
