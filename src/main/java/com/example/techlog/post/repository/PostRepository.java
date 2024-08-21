package com.example.techlog.post.repository;

import com.example.techlog.post.domain.Post;
import com.example.techlog.post.dto.PostSimpleResponse;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom {

    @Query("select p from Post p join fetch p.writer where p.isDeleted = false and p.id = :postId")
    Optional<Post> getPostWithWriter(@Param("postId") Long postId);

    @Query(
            value = "select p from Post p left join fetch p.writer order by p.id desc ",
            countQuery = "select count(p.id) from Post p where p.isDeleted = false"
    )
    Page<Post> getPostPageWithWriterPage2(Pageable pageable);

}
