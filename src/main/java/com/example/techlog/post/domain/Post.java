package com.example.techlog.post.domain;

import com.example.techlog.common.entity.BaseEntity;
import com.example.techlog.post.dto.PostUpdateRequest;
import com.example.techlog.tag.domain.PostTag;
import com.example.techlog.user.domain.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
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

    @Column(length = 10000)
    private String content;

    private String thumbnail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User writer;

    @OneToMany(mappedBy = "post")
    private final List<PostTag> postTags = new ArrayList<>();

    public Post(String title, String content, String thumbnail, User user) {
        this.title = title;
        this.content = content;
        this.thumbnail = thumbnail;
        this.writer = user;
        user.addPost(this);
    }

    public void update(PostUpdateRequest postUpdateRequest) {
        this.title = postUpdateRequest.title();
        this.content = postUpdateRequest.content();
        this.thumbnail = postUpdateRequest.thumbnail();
    }

    public void add(PostTag postTag) {
        this.postTags.add(postTag);
    }
}
