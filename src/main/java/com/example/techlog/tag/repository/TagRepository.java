package com.example.techlog.tag.repository;

import com.example.techlog.tag.domain.Tag;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TagRepository extends JpaRepository<Tag, Long> {

    @Query("select t from Tag t where t.content = :content order by t.id limit 1")
    Optional<Tag> findByContent(@Param("content") String content);

    @Query("select t from Tag t where t.user.id = :userId")
    List<Tag> findAllByUserId(@Param("userId") Long userId);

}
