package com.example.techlog.post.dto;

import com.example.techlog.tag.dto.TagDto;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public record PostUpdateRequest(

        @NotEmpty(message = "제목은 필수 입니다.")
        String title,
        @NotEmpty(message = "본문 내용은 필수 입니다.")
        String content,
        String thumbnail,

        @Nullable
        List<TagDto> tags,

        @Nullable
        List<String> newUrls
) {
}
