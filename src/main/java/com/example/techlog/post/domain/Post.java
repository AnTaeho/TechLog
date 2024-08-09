package com.example.techlog.post.domain;

import com.example.techlog.common.entity.BaseEntity;
import com.example.techlog.post.dto.PostUpdateRequest;
import com.example.techlog.user.domain.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    private String title;
    private String description;

    @Column(length = 10000)
    private String content;

    private String thumbnail;

    private boolean isDeleted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User writer;

    public Post(String title, String description, String content, String thumbnail, User user) {
        this.title = title;
        this.description = description;
        this.content = content;
        this.thumbnail = thumbnail;
        this.isDeleted = false;
        this.writer = user;
        user.addPost(this);
    }

    public void update(PostUpdateRequest postUpdateRequest) {
        this.title = postUpdateRequest.title();
        this.description = postUpdateRequest.description();
        this.content = postUpdateRequest.content();
    }

    public void delete() {
        this.isDeleted = true;
    }
}
