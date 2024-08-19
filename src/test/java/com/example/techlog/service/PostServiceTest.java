package com.example.techlog.service;

import com.example.techlog.post.domain.Post;
import com.example.techlog.post.dto.*;
import com.example.techlog.post.repository.PostRepository;
import com.example.techlog.post.service.PostService;
import com.example.techlog.tag.dto.TagDto;
import com.example.techlog.tag.repository.PostTagRepository;
import com.example.techlog.tag.repository.TagRepository;
import com.example.techlog.user.domain.User;
import com.example.techlog.user.repository.UserRepository;
import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
public class PostServiceTest {

    @Autowired
    private PostService postService;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private PostTagRepository postTagRepository;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = userRepository.save(new User("test@example.com", "password", "Test User"));
    }

    @Test
    void shouldWritePostAndReturnPostId() {
        // given
        PostWriteRequest postWriteRequest = new PostWriteRequest("Test Title", "Test Content", "Test Thumbnail", List.of(new TagDto("tag1"), new TagDto("tag2")));

        // when
        PostIdResponse response = postService.writePost(postWriteRequest, testUser.getEmail());

        // then
        assertThat(response).isNotNull();
        assertThat(response.postId()).isNotNull();

        Post savedPost = postRepository.findById(response.postId()).orElseThrow();
        assertThat(savedPost.getTitle()).isEqualTo("Test Title");
        assertThat(savedPost.getWriter().getEmail()).isEqualTo(testUser.getEmail());
//        assertThat(savedPost.().size()).isEqualTo(2);
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        // given
        PostWriteRequest postWriteRequest = new PostWriteRequest("Test Title", "Test Content", "Test Thumbnail", List.of());

        // when & then
        assertThrows(IllegalArgumentException.class, () -> {
            postService.writePost(postWriteRequest, "nonexistent@example.com");
        });
    }

    @Test
    void shouldReturnPostDetail() {
        // given
        Post post = new Post("Title", "Content", "Thumbnail", testUser);
        postRepository.save(post);

        // when
        PostDetailResponse postDetail = postService.getPostDetail(post.getId());

        // then
        assertThat(postDetail).isNotNull();
        assertThat(postDetail.title()).isEqualTo("Title");
        assertThat(postDetail.writer()).isEqualTo(testUser.getName());
    }

    @Test
    void shouldUpdatePost() {
        // given
        Post post = new Post("Old Title", "Old Content", "Old Thumbnail", testUser);
        postRepository.save(post);

        PostUpdateRequest updateRequest = new PostUpdateRequest("New Title", "New Content", "New Thumbnail", new ArrayList<>());

        // when
        postService.updatePost(post.getId(), updateRequest, "test@example.com");

        // then
        Post updatedPost = postRepository.findById(post.getId()).orElseThrow();
        assertThat(updatedPost.getTitle()).isEqualTo("New Title");
        assertThat(updatedPost.getContent()).isEqualTo("New Content");
    }

    @Test
    void shouldDeletePost() {
        // given
        Post post = new Post("Title", "Content", "Thumbnail", testUser);
        postRepository.save(post);

        // when
        postService.deletePost(post.getId());

        // then
        Post deletedPost = postRepository.findById(post.getId()).orElse(null);
        assertThat(deletedPost.isDeleted()).isTrue();
//        assertThat(postTagRepository.findAllByPost(post)).isEmpty();
    }

    @Test
    void shouldFindAllPosts() {
        // given
        Post post1 = postRepository.save(new Post("Title1", "Content1", "Thumbnail1", testUser));
        Post post2 = postRepository.save(new Post("Title2", "Content2", "Thumbnail2", testUser));

        // when
        Page<PostSimpleResponse> posts = postService.findAllPost(PageRequest.of(0, 2));

        // then
        assertThat(posts.getContent()).hasSize(2);
        assertThat(posts.getContent().get(1).title()).isEqualTo("Title1");
        assertThat(posts.getContent().get(0).title()).isEqualTo("Title2");
    }

    @Test
    void shouldFindPostsByIds() {
        // given
        Post post1 = postRepository.save(new Post("Title1", "Content1", "Thumbnail1", testUser));
        Post post2 = postRepository.save(new Post("Title2", "Content2", "Thumbnail2", testUser));

        List<Long> ids = List.of(post1.getId(), post2.getId());

        // when
        Page<PostSimpleResponse> posts = postService.findAllByIds(ids, PageRequest.of(0, 2));

        // then
        assertThat(posts.getContent()).hasSize(2);
    }

    @Test
    void shouldReturnEmptyPageWhenNoIdsProvided() {
        // when
        Page<PostSimpleResponse> posts = postService.findAllByIds(List.of(), PageRequest.of(0, 10));

        // then
        assertThat(posts.getContent()).isEmpty();
    }
}
