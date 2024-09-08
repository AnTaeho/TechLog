package com.example.techlog.image.repository;

import com.example.techlog.image.domain.ImageOutboxEvent;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ImageOutboxEventRepository extends JpaRepository<ImageOutboxEvent, Long> {

    @Modifying
    @Query("update ImageOutboxEvent set publishStatus = 'EXECUTED' where imageUrl = :url and postId = :postId")
    void updateExecuted(@Param("url") String url, @Param("postId") Long postId);

    @Query("select ioe from ImageOutboxEvent ioe where ioe.publishStatus = 'PUBLISHED'")
    List<ImageOutboxEvent> findAllFailEvent();

    @Modifying
    @Query("DELETE FROM ImageOutboxEvent e WHERE e.createdAt < :oneHourAgo")
    void deleteOlderThan(LocalDateTime oneHourAgo);
}
