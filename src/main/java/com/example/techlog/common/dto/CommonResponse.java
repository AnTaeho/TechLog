package com.example.techlog.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record CommonResponse<T>(T result, PageInfo page) {

    public CommonResponse(T result) {
        this(result, null);
    }

    public static final CommonResponse<EmptyDto> EMPTY = new CommonResponse<>(new EmptyDto());
}
