package com.example.techlog.domain;

import com.example.techlog.post.domain.Post;
import com.example.techlog.tag.domain.Tag;
import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

import com.example.techlog.user.domain.User;
import com.example.techlog.post.dto.PostUpdateRequest;
import com.example.techlog.tag.domain.PostTag;

class PostTest {

    private Post post;
    private User user;

    @BeforeEach
    void setUp() {
        user = new User("test@example.com", "password123", "John Doe");
        post = new Post("Initial Title", "Initial Content", "Initial Thumbnail", user);
    }

    @Test
    void testPostInitialization() {
        assertThat(post.getTitle()).isEqualTo("Initial Title");
        assertThat(post.getContent()).isEqualTo("Initial Content");
        assertThat(post.getThumbnail()).isEqualTo("Initial Thumbnail");
        assertThat(post.isDeleted()).isFalse();
        assertThat(post.getWriter()).isEqualTo(user);
        assertThat(post.getPostTags()).isEmpty();
    }

    @Test
    void testUpdatePost() {
        // Given
        PostUpdateRequest updateRequest = new PostUpdateRequest("Updated Title", "Updated Content", "Updated Thumbnail", new ArrayList<>());

        // When
        post.update(updateRequest);

        // Then
        assertThat(post.getTitle()).isEqualTo("Updated Title");
        assertThat(post.getContent()).isEqualTo("Updated Content");
        assertThat(post.getThumbnail()).isEqualTo("Updated Thumbnail");
    }

    @Test
    void testDeletePost() {
        // When
        post.delete();

        // Then
        assertThat(post.isDeleted()).isTrue();
    }

    @Test
    void testAddPostTag() {
        // Given
        PostTag postTag = new PostTag(post, new Tag("content", user));

        // When
        post.add(postTag);

        // Then
        assertThat(post.getPostTags()).contains(postTag);
    }

    @Test
    void testPostAssociatedWithUser() {
        assertThat(user.getPosts()).contains(post);
    }
}

