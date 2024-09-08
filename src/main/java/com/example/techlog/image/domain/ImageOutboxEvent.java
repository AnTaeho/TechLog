package com.example.techlog.image.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class ImageOutboxEvent {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_outbox_event_id")
    private Long id;

    private String imageUrl;
    private Long postId;

    @Enumerated(EnumType.STRING)
    private PublishStatus publishStatus;

    @Column(updatable = false)
    @CreatedDate
    private LocalDateTime createdAt;

    public ImageOutboxEvent(String imageUrl, Long postId) {
        this.imageUrl = imageUrl;
        this.postId = postId;
        this.publishStatus = PublishStatus.PUBLISHED;
    }
}
