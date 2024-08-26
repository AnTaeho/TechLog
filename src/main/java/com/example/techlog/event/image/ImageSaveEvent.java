package com.example.techlog.event.image;

import java.util.List;

public record ImageSaveEvent(
        List<String> values,
        Long postId
) {
}
