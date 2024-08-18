package com.example.techlog.post.dto;

import com.example.techlog.tag.dto.TagDto;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public record PostWriteRequest(
        @NotEmpty
        String title,
        @NotEmpty
        String content,
        String thumbnail,
        List<TagDto> tags
) {
}
