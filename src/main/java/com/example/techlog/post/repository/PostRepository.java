package com.example.techlog.post.repository;

import com.example.techlog.post.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom {

    @Query(
            value = "select p from Post p where p.title like 'a%'",
            countQuery = "select count(p.id) from Post p where p.title like 'a%'"
    )
    Page<Post> findAll(Pageable pageable);
}
