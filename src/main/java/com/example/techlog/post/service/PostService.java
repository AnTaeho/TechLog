package com.example.techlog.post.service;

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
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final TagRepository tagRepository;
    private final PostTagRepository postTagRepository;

    @Transactional
    public PostIdResponse writePost(PostWriteRequest postWriteRequest, String email) {
        User user = getUser(email);
        Post post = new Post(
                postWriteRequest.title(),
                postWriteRequest.description(),
                postWriteRequest.content(),
                postWriteRequest.thumbnail(),
                user
        );

        Post savedPost = postRepository.save(post);
        processingTags(postWriteRequest.tags(), user, savedPost);
        return new PostIdResponse(savedPost.getId());
    }

    private void processingTags(List<TagDto> tags, User user, Post post) {
        for (TagDto dto : tags) {
            Optional<Tag> byContent = tagRepository.findByContent(dto.content());
            System.out.println(dto.content());
            Tag tag = byContent.orElseGet(() -> tagRepository.save(new Tag(dto.content(), user)));
            postTagRepository.save(new PostTag(post, tag));
        }
    }

    public PostDetailResponse getPostDetail(Long postId) {
        Post post = getPostWithWriter(postId);
        return new PostDetailResponse(
                postId,
                post.getTitle(),
                post.getContent(),
                post.getThumbnail(),
                post.getWriter().getName(),
                post.getWriter().getId(),
                post.getCreatedDate().toLocalDate()
        );
    }

    public Page<PostSimpleResponse> findAllPost(Pageable pageable) {
        return postRepository.getPostPageWithWriterPage(pageable);
    }

    @Transactional
    public PostIdResponse updatePost(Long postId, PostUpdateRequest postUpdateRequest) {
        Post post = getPost(postId);
        post.update(postUpdateRequest);
        return new PostIdResponse(post.getId());
    }

    @Transactional
    public void deletePost(Long postId) {
        Post post = getPost(postId);
        post.delete();
    }

    private User getUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저를 찾을 수 없습니다."));
    }

    private Post getPost(Long postId) {
        return postRepository.findById(postId)
                .filter(it -> !it.isDeleted())
                .orElseThrow(() -> new IllegalArgumentException("해당 포스트를 찾을 수 없습니다."));
    }

    private Post getPostWithWriter(Long postId) {
        return postRepository.getPostWithWriter(postId)
                .filter(it -> !it.isDeleted())
                .orElseThrow(() -> new IllegalArgumentException("해당 포스트를 찾을 수 없습니다."));
    }

}
