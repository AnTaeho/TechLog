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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class PostServiceTest {

    @Autowired
    private PostService postService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private PostTagRepository postTagRepository;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private ApplicationEventPublisher publisher;

    @BeforeEach
    void setUp() {
        // Initial setup can be done here if needed
    }

    @Test
    void writePost() {
        // given
        PostWriteRequest request = new PostWriteRequest("title", "content", "thumbnail", List.of(new TagDto("http://image.com/1.jpg")), List.of("tag1"));
        String email = "test@example.com";
        User user = new User(email, "password", "name");
        userRepository.save(user);

        // when
        PostIdResponse response = postService.writePost(request, email);

        // then
        assertNotNull(response);
        assertNotNull(response.postId());
        assertTrue(postRepository.findById(response.postId()).isPresent());
    }

    @Test
    void getPostDetail() {
        // given
        User user = userRepository.save(new User("test@example.com", "password", "name"));
        Post post = new Post("title", "content", "thumbnail", user);
        Post savedPost = postRepository.save(post);

        // when
        PostDetailResponse response = postService.getPostDetail(savedPost.getId());

        // then
        assertNotNull(response);
        assertEquals(savedPost.getTitle(), response.title());
    }

    @Test
    void findAllPost() {
        // given
        PageRequest pageable = PageRequest.of(0, 10);
        User user1 = userRepository.save(new User("user1@example.com", "password", "name"));
        User user2 = userRepository.save(new User("user2@example.com", "password", "name"));
        postRepository.save(new Post("title1", "content1", "thumbnail1", user1));
        postRepository.save(new Post("title2", "content2", "thumbnail2", user2));

        // when
        Page<PostSimpleResponse> response = postService.findAllPost(pageable);

        // then
        assertNotNull(response);
        assertFalse(response.isEmpty());
        assertEquals(2, response.getTotalElements());
    }

    @Test
    void updatePost() {
        // given
        User user = userRepository.save(new User("test@example.com", "password", "name"));
        Post post = new Post("original title", "original content", "original thumbnail", user);
        Post savedPost = postRepository.save(post);
        PostUpdateRequest updateRequest = new PostUpdateRequest("updated title", "updated content", "updated thumbnail", List.of(new TagDto("tag2")), List.of("http://image.com/2.jpg"));

        // when
        PostIdResponse response = postService.updatePost(savedPost.getId(), updateRequest, savedPost.getWriter().getEmail());

        // then
        assertNotNull(response);
        Post updatedPost = postRepository.findById(response.postId()).orElseThrow();
        assertEquals("updated title", updatedPost.getTitle());
    }

    @Test
    void deletePost() {
        // given
        User user = userRepository.save(new User("test@example.com", "password", "name"));
        Post post = new Post("title", "content", "thumbnail", user);
        Post savedPost = postRepository.save(post);

        // when
        postService.deletePost(savedPost.getId(), "test@example.com");

        // then
        assertFalse(postRepository.findById(savedPost.getId()).isPresent());
    }

    @Test
    void findAllByIds() {
        // given
        User user1 = userRepository.save(new User("user1@example.com", "password", "name"));
        User user2 = userRepository.save(new User("user2@example.com", "password", "name"));
        Post post1 = postRepository.save(new Post("title1", "content1", "thumbnail1", user1));
        Post post2 = postRepository.save(new Post("title2", "content2", "thumbnail2", user2));
        List<Long> ids = List.of(post1.getId(), post2.getId());

        // when
        Page<PostSimpleResponse> response = postService.findAllByIds(ids, PageRequest.of(0, 10));

        // then
        assertNotNull(response);
        assertEquals(2, response.getTotalElements());
    }
}
