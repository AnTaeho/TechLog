package com.example.techlog.tag.dto;

import jakarta.validation.constraints.NotNull;

public record TagCreateRequest(
        @NotNull
        String content
) {
}
