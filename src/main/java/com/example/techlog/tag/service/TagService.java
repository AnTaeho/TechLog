package com.example.techlog.tag.service;

import com.example.techlog.tag.domain.Tag;
import com.example.techlog.tag.dto.TagCreateRequest;
import com.example.techlog.tag.dto.TagIdResponse;
import com.example.techlog.tag.repository.TagRepository;
import com.example.techlog.user.domain.User;
import com.example.techlog.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TagService {

    private final UserRepository userRepository;
    private final TagRepository tagRepository;

    @Transactional
    public TagIdResponse createTag(String email, TagCreateRequest tagCreateRequest) {
        User user = getUser(email);
        Tag tag = new Tag(tagCreateRequest.content(), user);
        Tag savedTag = tagRepository.save(tag);
        return new TagIdResponse(savedTag.getId());
    }

    @Transactional
    public void deleteTag(Long tagId) {
        tagRepository.deleteById(tagId);
    }

    private User getUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저를 찾을 수 없습니다."));
    }

}
