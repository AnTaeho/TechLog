package com.example.techlog.post.service;

import com.example.techlog.post.domain.Post;
import com.example.techlog.post.dto.PostDetailResponse;
import com.example.techlog.post.dto.PostIdResponse;
import com.example.techlog.post.dto.PostSimpleResponse;
import com.example.techlog.post.dto.PostUpdateRequest;
import com.example.techlog.post.dto.PostWriteRequest;
import com.example.techlog.post.repository.PostRepository;
import com.example.techlog.user.domain.User;
import com.example.techlog.user.repository.UserRepository;
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

    @Transactional
    public PostIdResponse writePost(PostWriteRequest postWriteRequest, String email) {
        Post post = new Post(
                postWriteRequest.title(),
                postWriteRequest.description(),
                postWriteRequest.content(),
                postWriteRequest.thumbnail(),
                getUser(email)
        );
        Post savedPost = postRepository.save(post);
        return new PostIdResponse(savedPost.getId());
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
