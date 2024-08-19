package com.example.techlog.tag.repository;

import com.example.techlog.tag.domain.PostTag;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostTagRepository extends JpaRepository<PostTag, Long> {

    @Modifying
    @Query("delete from PostTag pt where pt.post.id = :post")
    void deleteAllByPost(@Param("post") Long post);

    @Query("select p.id from PostTag pt join fetch Post p on pt.post.id = p.id where pt.tag.id = :tagId")
    List<Long> findByTagId(@Param("tagId") Long tagId);

}
