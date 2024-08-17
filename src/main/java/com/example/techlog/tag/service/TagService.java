package com.example.techlog.tag.service;

import com.example.techlog.tag.domain.Tag;
import com.example.techlog.tag.dto.TagCreateRequest;
import com.example.techlog.tag.dto.TagDto;
import com.example.techlog.tag.dto.TagIdResponse;
import com.example.techlog.tag.repository.PostTagRepository;
import com.example.techlog.tag.repository.TagRepository;
import com.example.techlog.user.domain.User;
import com.example.techlog.user.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TagService {

    private final UserRepository userRepository;
    private final TagRepository tagRepository;
    private final PostTagRepository postTagRepository;

    public List<Long> searchByTag(TagDto tagDto) {
        Optional<Tag> tag = tagRepository.findByContent(tagDto.content());
        if (tag.isEmpty()) {
            return new ArrayList<>();
        }
        return postTagRepository.findByTagId(tag.get().getId());
    }

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
