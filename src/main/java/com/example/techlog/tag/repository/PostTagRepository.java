package com.example.techlog.tag.repository;

import com.example.techlog.tag.domain.PostTag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostTagRepository extends JpaRepository<PostTag, Long> {
}
