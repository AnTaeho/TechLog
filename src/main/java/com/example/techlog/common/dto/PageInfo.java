package com.example.techlog.common.dto;

import org.springframework.data.domain.Page;

public record PageInfo(
        int size,
        int number,
        long totalElements,
        int totalPages
) {
    public static PageInfo of(Page<?> page) {
        return new PageInfo(
                page.getSize(),
                page.getNumber(),
                page.getTotalElements(),
                page.getTotalPages()
        );
    }
}
