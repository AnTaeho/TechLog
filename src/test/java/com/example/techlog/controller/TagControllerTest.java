package com.example.techlog.controller;

import com.example.techlog.common.dto.CommonResponse;
import com.example.techlog.common.dto.EmptyDto;
import com.example.techlog.tag.controller.TagController;
import com.example.techlog.tag.dto.TagCreateRequest;
import com.example.techlog.tag.dto.TagIdResponse;
import com.example.techlog.tag.dto.TagListResponse;
import com.example.techlog.tag.service.TagService;
import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TagControllerTest {

    @Mock
    private TagService tagService;

    @InjectMocks
    private TagController tagController;

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
    @DisplayName("태그 생성 테스트")
    void createTag() {
        // given
        TagCreateRequest tagCreateRequest = new TagCreateRequest("content");
        TagIdResponse tagIdResponse = new TagIdResponse(1L);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("testUser");
        when(tagService.createTag(anyString(), any(TagCreateRequest.class))).thenReturn(tagIdResponse);

        // when
        CommonResponse<TagIdResponse> response = tagController.createTag(tagCreateRequest);

        // then
        assertEquals(tagIdResponse, response.result());
        verify(tagService, times(1)).createTag(anyString(), any(TagCreateRequest.class));
        verify(securityContext, times(1)).getAuthentication();
        verify(authentication, times(1)).getName();
    }

    @Test
    @DisplayName("태그 조회 테스트")
    void getMyTags() {
        // given
        TagListResponse tagListResponse = new TagListResponse(new ArrayList<>());
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("testUser");
        when(tagService.getMyTags(anyString())).thenReturn(tagListResponse);

        // when
        CommonResponse<TagListResponse> response = tagController.getMyTags();

        // then
        assertEquals(tagListResponse, response.result());
        verify(tagService, times(1)).getMyTags(anyString());
        verify(securityContext, times(1)).getAuthentication();
        verify(authentication, times(1)).getName();
    }

    @Test
    void deleteTag() {
        // given
        Long tagId = 1L;

        // when
        CommonResponse<EmptyDto> response = tagController.deleteTag(tagId);

        // then
        assertEquals(CommonResponse.EMPTY, response);
        verify(tagService, times(1)).deleteTag(tagId);
    }
}
