package com.example.techlog.domain;

import com.example.techlog.image.domain.Image;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class ImageTest {

    @Test
    void constructor_shouldInitializeFieldsCorrectly() {
        // given
        String imageUrl = "http://example.com/image.jpg";
        Long postId = 1L;

        // when
        Image image = new Image(imageUrl, postId);

        // then
        assertEquals(imageUrl, image.getImageUrl());
        assertEquals(postId, image.getPostId());
        assertNull(image.getId());
    }

}