package com.example.techlog.tag.repository;

import com.example.techlog.tag.domain.PostTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostTagRepository extends JpaRepository<PostTag, Long> {

    @Modifying
    @Query("delete from PostTag pt where pt.post.id = :post")
    void deleteAllByPost(@Param("post") Long post);

}
