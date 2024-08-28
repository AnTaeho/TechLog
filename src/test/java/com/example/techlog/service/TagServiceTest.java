package com.example.techlog.service;

import com.example.techlog.tag.domain.Tag;
import com.example.techlog.tag.dto.TagListResponse;
import com.example.techlog.tag.repository.TagRepository;
import com.example.techlog.tag.service.TagService;
import com.example.techlog.user.domain.User;
import com.example.techlog.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class TagServiceTest {

    @Autowired
    private TagService tagService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TagRepository tagRepository;

    private User user;

    @BeforeEach
    void setUp() {
        // given
        user = new User("test@example.com", "password", "name");
        userRepository.save(user);
    }

    @Test
    void searchByTag() {
        // given
        Tag tag = new Tag("testTag", user);
        tagRepository.save(tag);

        // when
        List<Long> result = tagService.searchByTag("testTag");

        // then
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void getMyTags() {
        // given
        Tag tag1 = new Tag("tag1", user);
        Tag tag2 = new Tag("tag2", user);
        tagRepository.save(tag1);
        tagRepository.save(tag2);

        // when
        TagListResponse response = tagService.getMyTags(user.getEmail());

        // then
        assertNotNull(response);
        assertEquals(2, response.tags().size());
        assertTrue(response.tags().contains("tag1"));
        assertTrue(response.tags().contains("tag2"));
    }

//    @Test
//    void createTag() {
//        // given
//        TagCreateRequest request = new TagCreateRequest("newTag");
//
//        // when
//        TagIdResponse response = tagService.createTag(user.getEmail(), request);
//
//        // then
//        assertNotNull(response);
//        assertNotNull(response.tagId());
//        assertTrue(tagRepository.findById(response.tagId()).isPresent());
//    }

    @Test
    void deleteTag() {
        // given
        Tag tag = new Tag("deletableTag", user);
        Tag savedTag = tagRepository.save(tag);

        // when
        tagService.deleteTag(savedTag.getId());

        // then
        assertFalse(tagRepository.findById(savedTag.getId()).isPresent());
    }

    @Test
    void getUserWithTag_shouldThrowExceptionIfUserNotFound() {
        // given
        String nonExistentEmail = "nonexistent@example.com";

        // when / then
        Exception exception = assertThrows(IllegalArgumentException.class, () -> tagService.getMyTags(nonExistentEmail));

        assertEquals("해당 유저를 찾을 수 없습니다.", exception.getMessage());
    }
}
