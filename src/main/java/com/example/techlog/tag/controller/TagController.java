package com.example.techlog.tag.controller;

import com.example.techlog.common.dto.CommonResponse;
import com.example.techlog.common.dto.EmptyDto;
import com.example.techlog.tag.dto.TagCreateRequest;
import com.example.techlog.tag.dto.TagIdResponse;
import com.example.techlog.tag.dto.TagListResponse;
import com.example.techlog.tag.service.TagService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tags")
public class TagController {

    private final TagService tagService;

    @PostMapping
    public CommonResponse<TagIdResponse> createTag(@Valid @RequestBody TagCreateRequest tagCreateRequest) {
        return new CommonResponse<>(tagService.createTag(getUserName(), tagCreateRequest));
    }

    @GetMapping
    public CommonResponse<TagListResponse> getMyTags() {
        return new CommonResponse<>(tagService.getMyTags(getUserName()));
    }

    @DeleteMapping("/{tagId}")
    public CommonResponse<EmptyDto> deleteTag(@PathVariable("tagId") Long tagId) {
        tagService.deleteTag(tagId);
        return CommonResponse.EMPTY;
    }

    private String getUserName() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

}
