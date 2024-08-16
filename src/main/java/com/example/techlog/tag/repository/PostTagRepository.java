package com.example.techlog.tag.repository;

import com.example.techlog.post.domain.Post;
import com.example.techlog.tag.domain.PostTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PostTagRepository extends JpaRepository<PostTag, Long> {

    @Query("delete from PostTag pt where pt.post = :post")
    void deleteAllByPost(Post post);

}
