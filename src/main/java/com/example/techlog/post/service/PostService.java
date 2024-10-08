package com.example.techlog.post.service;

import com.example.techlog.common.RestPage;
import com.example.techlog.error.custom.CriticalException;
import com.example.techlog.event.image.ImageSaveEvent;
import com.example.techlog.post.domain.Post;
import com.example.techlog.post.dto.PostDetailResponse;
import com.example.techlog.post.dto.PostIdResponse;
import com.example.techlog.post.dto.PostSimpleResponse;
import com.example.techlog.post.dto.PostUpdateRequest;
import com.example.techlog.post.dto.PostWriteRequest;
import com.example.techlog.post.repository.PostRepository;
import com.example.techlog.tag.domain.PostTag;
import com.example.techlog.tag.domain.Tag;
import com.example.techlog.tag.dto.TagDto;
import com.example.techlog.tag.repository.PostTagRepository;
import com.example.techlog.tag.repository.TagRepository;
import com.example.techlog.user.domain.User;
import com.example.techlog.user.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {

    private static final String MAIN_PAGE_CACHE_KEY = "main_page_posts";
    private static final long CACHE_EXPIRATION = 3600;

    private final RedisTemplate<String, Object> redisTemplate;

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final TagRepository tagRepository;
    private final PostTagRepository postTagRepository;
    private final ApplicationEventPublisher publisher;

    @Transactional
    public PostIdResponse writePost(PostWriteRequest postWriteRequest, String email) {
        User user = getUser(email);
        Post post = new Post(
                postWriteRequest.title(),
                postWriteRequest.content(),
                postWriteRequest.thumbnail(),
                user
        );

        Post savedPost = postRepository.save(post);
        publisher.publishEvent(new ImageSaveEvent(postWriteRequest.urls(), savedPost.getId()));
        processingTags(postWriteRequest.tags(), user, savedPost);
        clearCache();
        return new PostIdResponse(savedPost.getId());
    }

    private void processingTags(List<TagDto> tags, User user, Post post) {
        if (tags == null || tags.isEmpty()) {
            return;
        }
        for (TagDto dto : tags) {
            Optional<Tag> byContent = tagRepository.findByContent(dto.content(), user.getId());
            Tag tag = byContent.orElseGet(() -> tagRepository.save(new Tag(dto.content(), user)));
            postTagRepository.save(new PostTag(post, tag));
        }
    }

    public PostDetailResponse getPostDetail(Long postId) {
        return postRepository.findPostWithFullInfo(postId);
    }

    public Page<PostSimpleResponse> findAllPost(Pageable pageable) {
        int pageNumber = pageable.getPageNumber();
        if (pageNumber > 4) {
            return postRepository.getPostPageWithWriterPage(pageable);
        }

        RestPage<PostSimpleResponse> cachedPosts = (RestPage<PostSimpleResponse>) redisTemplate.opsForValue().get(MAIN_PAGE_CACHE_KEY + pageNumber);
        if (cachedPosts != null) {
            log.info("Using cached data page: {}", pageNumber);
            return cachedPosts;
        }

        RestPage<PostSimpleResponse> postPageWithWriterPage = postRepository.getPostPageWithWriterPage(pageable);
        redisTemplate.opsForValue().set(MAIN_PAGE_CACHE_KEY + pageNumber, postPageWithWriterPage, CACHE_EXPIRATION, TimeUnit.SECONDS);
        return postPageWithWriterPage;
    }


    @Transactional
    public PostIdResponse updatePost(Long postId, PostUpdateRequest postUpdateRequest, String userName) {
        Post post = getPost(postId);
        User user = getUser(userName);

        if (!post.getWriter().getId().equals(user.getId())) {
            throw new CriticalException(HttpStatus.INTERNAL_SERVER_ERROR, "허용되지 않은 사용자가 수정 요청을 했습니다.");
        }

        post.update(postUpdateRequest);

        publisher.publishEvent(new ImageSaveEvent(postUpdateRequest.newUrls(), post.getId()));
        postTagRepository.deleteAllByPost(postId);
        processingTags(postUpdateRequest.tags(), user, post);
        clearCache();
        return new PostIdResponse(post.getId());
    }

    @Transactional
    public void deletePost(Long postId, String email) {
        Post post = getPost(postId);
        User user = getUser(email);

        if (!post.getWriter().getId().equals(user.getId())) {
            throw new CriticalException(HttpStatus.INTERNAL_SERVER_ERROR, "허용되지 않은 사용자가 수정 요청을 했습니다.");
        }

        List<Long> tagsOfPost = post.getPostTags().stream()
                .map(it -> it.getTag().getId())
                .toList();
        postTagRepository.deleteAllByPost(post.getId());
        List<Long> remainingTagIds = userRepository.findRemainingTags(user.getId(), postId, tagsOfPost);
        tagsOfPost.stream()
                .filter(tagId -> !remainingTagIds.contains(tagId))
                .forEach(tagRepository::deleteById);
        postRepository.delete(post);
        clearCache();
    }

    public Page<PostSimpleResponse> findAllByIds(List<Long> ids, Pageable pageable) {
        if (ids.isEmpty()) {
            return new PageImpl<>(new ArrayList<>(), pageable, 0L);
        }

        return postRepository.searchByIds(ids, pageable);
    }

    private User getUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저를 찾을 수 없습니다."));
    }

    private Post getPost(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 포스트를 찾을 수 없습니다."));
    }

    private void clearCache() {
        redisTemplate.delete(MAIN_PAGE_CACHE_KEY + 0);
        redisTemplate.delete(MAIN_PAGE_CACHE_KEY + 1);
        redisTemplate.delete(MAIN_PAGE_CACHE_KEY + 2);
        redisTemplate.delete(MAIN_PAGE_CACHE_KEY + 3);
        redisTemplate.delete(MAIN_PAGE_CACHE_KEY + 4);
    }
}
