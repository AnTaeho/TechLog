package com.example.techlog.post.controller;

import com.example.techlog.common.dto.CommonResponse;
import com.example.techlog.common.dto.EmptyDto;
import com.example.techlog.common.dto.PageInfo;
import com.example.techlog.post.dto.PostDetailResponse;
import com.example.techlog.post.dto.PostIdResponse;
import com.example.techlog.post.dto.PostListResponse;
import com.example.techlog.post.dto.PostSimpleResponse;
import com.example.techlog.post.dto.PostUpdateRequest;
import com.example.techlog.post.dto.PostWriteRequest;
import com.example.techlog.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    public CommonResponse<PostIdResponse> writePost(@RequestBody PostWriteRequest postWriteRequest) {
        return new CommonResponse<>(postService.writePost(postWriteRequest, getUserName()));
    }

    @GetMapping("/{postId}")
    public CommonResponse<PostDetailResponse> getPostDetail(@PathVariable("postId") Long postId) {
        return new CommonResponse<>(postService.getPostDetail(postId));
    }

    @GetMapping
    public CommonResponse<PostListResponse> getAllPost(Pageable pageable) {
        Page<PostSimpleResponse> allPost = postService.findAllPost(pageable);
        return new CommonResponse<>(
                new PostListResponse(allPost.getContent()),
                PageInfo.of(allPost)
        );
    }

    @PatchMapping("/{postId}")
    public CommonResponse<PostIdResponse> updatePost(@PathVariable("postId") Long postId, @RequestBody PostUpdateRequest postUpdateRequest) {
        return new CommonResponse<>(postService.updatePost(postId, postUpdateRequest));
    }

    @DeleteMapping("/{postId}")
    public CommonResponse<EmptyDto> deletePost(@PathVariable("postId") Long postId) {
        postService.deletePost(postId);
        return CommonResponse.EMPTY;
    }

    private String getUserName() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

}
