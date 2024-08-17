package com.example.techlog.post.repository;

import static com.example.techlog.post.domain.QPost.*;

import com.example.techlog.post.domain.Post;
import com.example.techlog.post.dto.PostSimpleResponse;
import com.example.techlog.user.domain.QUser;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import org.springframework.data.domain.Page;
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
    public Page<PostSimpleResponse> getPostPageWithWriterPage(Pageable pageable) {
        JPAQuery<Post> query = queryFactory.selectFrom(post)
                .leftJoin(post.writer, QUser.user).fetchJoin()
                .where(post.isDeleted.eq(false));

        long count = queryFactory.select(post.id).from(post).where(post.isDeleted.eq(false)).fetch().size();

        List<PostSimpleResponse> results = query
                .orderBy(post.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize()).stream()
                .map(this::toSimpleResponse)
                .toList();

        return new PageImpl<>(results, pageable, count);
    }

    @Override
    public Page<PostSimpleResponse> searchByIds(List<Long> ids, Pageable pageable) {
        JPAQuery<Post> query = queryFactory.selectFrom(post)
                .where(post.id.in(ids));

        long size = query.fetch().size();

        List<PostSimpleResponse> result = query.orderBy(post.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize()).stream()
                .map(this::toSimpleResponse)
                .toList();

        return new PageImpl<>(result, pageable, size);

    }

    private PostSimpleResponse toSimpleResponse(Post post) {
        return new PostSimpleResponse(
                post.getId(),
                post.getTitle(),
                post.getDescription(),
                post.getThumbnail(),
                post.getWriter().getName(),
                post.getCreatedDate().toLocalDate()
        );
    }


}
