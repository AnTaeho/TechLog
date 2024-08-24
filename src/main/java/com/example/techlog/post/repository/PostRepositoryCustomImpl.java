package com.example.techlog.post.repository;

import static com.example.techlog.post.domain.QPost.post;
import static com.example.techlog.tag.domain.QPostTag.*;
import static com.example.techlog.user.domain.QUser.*;

import com.example.techlog.common.RestPage;
import com.example.techlog.post.domain.Post;
import com.example.techlog.post.dto.PostDetailResponse;
import com.example.techlog.post.dto.PostSimpleResponse;
import com.example.techlog.tag.domain.QTag;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public class PostRepositoryCustomImpl implements PostRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public PostRepositoryCustomImpl(JPAQueryFactory jpaQueryFactory) {
        this.queryFactory = jpaQueryFactory;
    }

    @Override
    public RestPage<PostSimpleResponse> getPostPageWithWriterPage(Pageable pageable) {
        List<Long> postIds = queryFactory
                .select(post.id)
                .from(post)
                .orderBy(post.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        if (postIds.isEmpty()) {
            return new RestPage<>(new PageImpl<>(Collections.emptyList(), pageable, 0));
        }

        List<Post> posts = queryFactory
                .selectFrom(post)
                .leftJoin(post.writer, user).fetchJoin()
                .leftJoin(post.postTags, postTag).fetchJoin()
                .leftJoin(postTag.tag, QTag.tag).fetchJoin()
                .where(post.id.in(postIds))
                .orderBy(post.id.desc())
                .fetch();

        long count = queryFactory
                .select(post.id)
                .from(post)
                .fetch().size();

        List<PostSimpleResponse> result = posts.stream()
                .map(this::toSimpleResponse)
                .toList();

        return new RestPage<>(new PageImpl<>(result, pageable, count));
    }

    private PostSimpleResponse toSimpleResponse(Post post) {
        return new PostSimpleResponse(
                post.getId(),
                post.getTitle(),
                post.getThumbnail(),
                post.getWriter().getName(),
                post.getCreatedDate().toLocalDate().toString(),
                post.getPostTags().stream()
                        .map(postTag -> postTag.getTag().getContent())
                        .toList()
        );
    }

    @Override
    public RestPage<PostSimpleResponse> searchByIds(List<Long> ids, Pageable pageable) {
        List<Long> postIds = queryFactory
                .select(post.id)
                .from(post)
                .where(post.id.in(ids))
                .orderBy(post.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        if (postIds.isEmpty()) {
            return new RestPage<>(new PageImpl<>(Collections.emptyList(), pageable, 0));
        }

        List<Post> posts = queryFactory
                .selectFrom(post)
                .leftJoin(post.writer, user).fetchJoin()
                .leftJoin(post.postTags, postTag).fetchJoin()
                .leftJoin(postTag.tag, QTag.tag).fetchJoin()
                .where(post.id.in(postIds))
                .orderBy(post.id.desc())
                .fetch();

        long total = queryFactory
                .select(post.id)
                .from(post)
                .where(post.id.in(ids))
                .fetch().size();

        List<PostSimpleResponse> result = posts.stream()
                .map(this::toSimpleResponse)
                .toList();

        return new RestPage<>(new PageImpl<>(result, pageable, total));
    }

    @Override
    public PostDetailResponse findPostWithFullInfo(Long postId) {
        Optional<Post> query = queryFactory
                .selectFrom(post)
                .leftJoin(post.writer, user).fetchJoin()
                .leftJoin(post.postTags, postTag).fetchJoin()
                .leftJoin(postTag.tag, QTag.tag).fetchJoin()
                .where(post.id.eq(postId))
                .stream().findFirst();

        if (query.isEmpty()) {
            throw new IllegalArgumentException("해당 게시글은 없습니다.");
        }

        Post result = query.get();

        return new PostDetailResponse(
                postId,
                result.getTitle(),
                result.getContent(),
                result.getThumbnail(),
                result.getWriter().getName(),
                result.getWriter().getId(),
                result.getCreatedDate().toLocalDate().toString(),
                result.getPostTags().stream()
                        .map(it -> it.getTag().getContent()).toList()
        );
    }

}
