package com.example.techlog.post.dto;

import java.time.LocalDate;

public record PostSimpleResponse(
        String title,
        String description,
        String writer,
        LocalDate createdAt
) {
}
