package com.example.techlog.tag.repository;

import com.example.techlog.post.domain.Post;
import com.example.techlog.tag.domain.PostTag;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostTagRepository extends JpaRepository<PostTag, Long> {

    @Query("delete from PostTag pt where pt.post = :post")
    void deleteAllByPost(Post post);

    @Query("select p.id from PostTag pt join fetch Post p on pt.post.id = p.id where pt.tag.id = :tagId")
    List<Long> findByTagId(@Param("tagId") Long tagId);

}
