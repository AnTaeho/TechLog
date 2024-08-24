package com.example.techlog.service;

import com.example.techlog.post.domain.Post;
import com.example.techlog.post.repository.PostRepository;
import com.example.techlog.tag.domain.PostTag;
import com.example.techlog.tag.domain.Tag;
import com.example.techlog.tag.dto.TagCreateRequest;
import com.example.techlog.tag.dto.TagIdResponse;
import com.example.techlog.tag.repository.PostTagRepository;
import com.example.techlog.tag.repository.TagRepository;
import com.example.techlog.tag.service.TagService;
import com.example.techlog.user.domain.User;
import com.example.techlog.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class TagServiceTest {

    @Autowired
    private TagService tagService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostTagRepository postTagRepository;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User("test@example.com", "password123", "John Doe");
        userRepository.save(user);
    }

    @Test
    void testCreateTag() {
        // Given
        TagCreateRequest request = new TagCreateRequest("Java");

        // When
        TagIdResponse response = tagService.createTag("an981022@naver.com", request);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.tagId()).isNotNull();

        Tag tag = tagRepository.findById(response.tagId()).orElse(null);
        assertThat(tag).isNotNull();
        assertThat(tag.getContent()).isEqualTo("Java");
        assertThat(tag.getUser()).isEqualTo(user);
    }

    @Test
    void testSearchByTagFound() {
        // Given
        Tag tag = new Tag("Spring Boot", user);
        tagRepository.save(tag);

        Post post = new Post("Title", "Content", "Thumbnail", user);
        postRepository.save(post);

        postTagRepository.save(new PostTag(post, tag));



        // When
        List<Long> postIds = tagService.searchByTag("Spring Boot");

        // Then
        assertThat(postIds).isNotEmpty();
    }

    @Test
    void testSearchByTagNotFound() {
        // When
        List<Long> postIds = tagService.searchByTag("Nonexistent Tag");

        // Then
        assertThat(postIds).isEmpty();
    }

    @Test
    void testDeleteTag() {
        // Given
        Tag tag = new Tag("DeleteMe", user);
        Tag savedTag = tagRepository.save(tag);

        // When
        tagService.deleteTag(savedTag.getId());

        // Then
        assertThat(tagRepository.findById(savedTag.getId())).isEmpty();
    }
}

