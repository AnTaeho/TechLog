package com.example.techlog.post.dto;

import java.util.List;

public record PostListResponse(
        List<PostSimpleResponse> posts
) {
}
