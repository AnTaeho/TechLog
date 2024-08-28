package com.example.techlog.controller;

import com.example.techlog.common.dto.CommonResponse;
import com.example.techlog.common.dto.EmptyDto;
import com.example.techlog.post.controller.PostController;
import com.example.techlog.post.dto.*;
import com.example.techlog.post.service.PostService;
import com.example.techlog.tag.service.TagService;
import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class PostControllerTest {

    @Mock
    private PostService postService;

    @Mock
    private TagService tagService;

    @InjectMocks
    private PostController postController;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void searchByTag() {
        // given
        String tag = "testTag";
        Pageable pageable = mock(Pageable.class);
        List<Long> ids = List.of(1L, 2L);
        Page<PostSimpleResponse> page = new PageImpl<>(List.of(new PostSimpleResponse(1L, "", "", "", "",new ArrayList<>())));
        when(tagService.searchByTag(anyString())).thenReturn(ids);
        when(postService.findAllByIds(anyList(), any(Pageable.class))).thenReturn(page);

        // when
        CommonResponse<PostListResponse> response = postController.searchByTag(tag, pageable);

        // then
        assertEquals(page.getContent(), response.result().posts());
        verify(tagService, times(1)).searchByTag(anyString());
        verify(postService, times(1)).findAllByIds(anyList(), any(Pageable.class));
    }

    @Test
    void writePost() {
        // given
        PostWriteRequest postWriteRequest = new PostWriteRequest("", "", "", new ArrayList<>(), new ArrayList<>());
        PostIdResponse postIdResponse = new PostIdResponse(1L);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("testUser");
        when(postService.writePost(any(PostWriteRequest.class), anyString())).thenReturn(postIdResponse);

        // when
        CommonResponse<PostIdResponse> response = postController.writePost(postWriteRequest);

        // then
        assertEquals(postIdResponse, response.result());
        verify(postService, times(1)).writePost(any(PostWriteRequest.class), anyString());
        verify(securityContext, times(1)).getAuthentication();
        verify(authentication, times(1)).getName();
    }

    @Test
    void getPostDetail() {
        // given
        Long postId = 1L;
        PostDetailResponse postDetailResponse = new PostDetailResponse(1L, "", "", "", "", 1L, "", new ArrayList<>());
        when(postService.getPostDetail(anyLong())).thenReturn(postDetailResponse);

        // when
        CommonResponse<PostDetailResponse> response = postController.getPostDetail(postId);

        // then
        assertEquals(postDetailResponse, response.result());
        verify(postService, times(1)).getPostDetail(anyLong());
    }

    @Test
    void getAllPost() {
        // given
        Pageable pageable = mock(Pageable.class);
        Page<PostSimpleResponse> page = new PageImpl<>(List.of(new PostSimpleResponse(1L, "", "", "", "",new ArrayList<>())));
        when(postService.findAllPost(any(Pageable.class))).thenReturn(page);

        // when
        CommonResponse<PostListResponse> response = postController.getAllPost(pageable);

        // then
        assertEquals(page.getContent(), response.result().posts());
        verify(postService, times(1)).findAllPost(any(Pageable.class));
    }

    @Test
    void updatePost() {
        // given
        Long postId = 1L;
        PostUpdateRequest postUpdateRequest = new PostUpdateRequest("", "", "", new ArrayList<>(), new ArrayList<>());
        PostIdResponse postIdResponse = new PostIdResponse(1L);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("testUser");
        when(postService.updatePost(anyLong(), any(PostUpdateRequest.class), anyString())).thenReturn(postIdResponse);

        // when
        CommonResponse<PostIdResponse> response = postController.updatePost(postId, postUpdateRequest);

        // then
        assertEquals(postIdResponse, response.result());
        verify(postService, times(1)).updatePost(anyLong(), any(PostUpdateRequest.class), anyString());
        verify(securityContext, times(1)).getAuthentication();
        verify(authentication, times(1)).getName();
    }

    @Test
    void deletePost() {
        // given
        Long postId = 1L;

        // when
        CommonResponse<EmptyDto> response = postController.deletePost(postId);

        // then
        assertEquals(CommonResponse.EMPTY, response);
        verify(postService, times(1)).deletePost(anyLong());
    }
}

