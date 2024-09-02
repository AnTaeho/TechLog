package com.example.techlog.tag.repository;

import com.example.techlog.tag.domain.Tag;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TagRepository extends JpaRepository<Tag, Long> {

    @Query("select t from Tag t where t.content = :content and t.user.id = :userId order by t.id limit 1")
    Optional<Tag> findByContent(@Param("content") String content, @Param("userId") Long userId);

    @Query("select t from Tag t join fetch t.postTags where t.content = :content")
    Optional<Tag> findByContentWithPostTag(@Param("content") String content);

}
