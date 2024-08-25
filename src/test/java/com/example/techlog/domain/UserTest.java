package com.example.techlog.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.techlog.post.domain.Post;
import com.example.techlog.tag.domain.Tag;
import com.example.techlog.user.domain.User;
import com.example.techlog.user.domain.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

class UserTest {

    private User user;
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        passwordEncoder = new BCryptPasswordEncoder();
        user = new User("test@example.com", "password123", "John Doe");
    }

    @Test
    void testUserInitialization() {
        assertThat(user.getEmail()).isEqualTo("test@example.com");
        assertThat(user.getPassword()).isEqualTo("password123");
        assertThat(user.getName()).isEqualTo("John Doe");
        assertThat(user.getRole()).isEqualTo(UserRole.USER);
        assertThat(user.isDeleted()).isFalse();
        assertThat(user.getPosts()).isEmpty();
        assertThat(user.getTags()).isEmpty();
    }

    @Test
    void testEncodePassword() {
        // When
        user.encodePassword(passwordEncoder);

        // Then
        assertThat(passwordEncoder.matches("password123", user.getPassword())).isTrue();
    }

    @Test
    void testAddPost() {
        // Given
        Post post = new Post("tital", "content", null, user);

        // When
        user.addPost(post);

        // Then
        assertThat(user.getPosts()).contains(post);
    }

    @Test
    void testAddTag() {
        // Given
        Tag tag = new Tag("content", user);

        // When
        user.addTag(tag);

        // Then
        assertThat(user.getTags()).contains(tag);
    }
}

