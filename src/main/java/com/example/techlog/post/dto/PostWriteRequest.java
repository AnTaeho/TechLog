package com.example.techlog.post.dto;

import com.example.techlog.tag.dto.TagDto;
import java.util.List;

public record PostWriteRequest(
        String title,
        String description,
        String content,
        String thumbnail,
        List<TagDto> tags
) {
}
