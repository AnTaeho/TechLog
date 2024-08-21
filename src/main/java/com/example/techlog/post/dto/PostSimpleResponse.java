package com.example.techlog.post.dto;

import com.example.techlog.post.domain.Post;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.List;

public record PostSimpleResponse(
        Long postId,
        String title,
        String thumbnail,
        String writer,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        String createdAt,

        List<String> tags
) {

        public static PostSimpleResponse of(Post post, List<String> tags) {
                return new PostSimpleResponse(
                        post.getId(),
                        post.getTitle(),
                        post.getThumbnail(),
                        post.getWriter().getName(),
                        post.getCreatedDate().toString(),
                        tags
                );
        }
}
