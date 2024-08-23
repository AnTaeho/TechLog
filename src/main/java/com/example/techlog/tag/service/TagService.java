package com.example.techlog.tag.service;

import com.example.techlog.tag.domain.PostTag;
import com.example.techlog.tag.domain.Tag;
import com.example.techlog.tag.dto.TagCreateRequest;
import com.example.techlog.tag.dto.TagIdResponse;
import com.example.techlog.tag.dto.TagListResponse;
import com.example.techlog.tag.repository.TagRepository;
import com.example.techlog.user.domain.User;
import com.example.techlog.user.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TagService {

    private final UserRepository userRepository;
    private final TagRepository tagRepository;

    public List<Long> searchByTag(String tagDto) {
        return tagRepository.findByContentWithPostTag(tagDto)
                .map(value -> value.getPostTags().stream()
                .map(PostTag::getId)
                .toList()).orElseGet(ArrayList::new);
    }

    public TagListResponse getMyTags(String email) {
        User user = getUserWithTag(email);
        List<String> result = user.getTags().stream()
                .map(Tag::getContent)
                .toList();
        return new TagListResponse(result);
    }

    @Transactional
    public TagIdResponse createTag(String email, TagCreateRequest tagCreateRequest) {
        User user = getUserWithTag(email);
        Tag tag = new Tag(tagCreateRequest.content(), user);
        Tag savedTag = tagRepository.save(tag);
        return new TagIdResponse(savedTag.getId());
    }

    @Transactional
    public void deleteTag(Long tagId) {
        tagRepository.deleteById(tagId);
    }

    private User getUserWithTag(String email) {
        return userRepository.getUserWithTag(email)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저를 찾을 수 없습니다."));
    }

}
