package com.example.techlog.post.repository;

import com.example.techlog.post.domain.Post;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom {

    @Query("select p from Post p join fetch p.writer where p.isDeleted = false and p.id = :postId")
    Optional<Post> getPostWithWriter(@Param("postId") Long postId);

}
