package com.example.techlog.post.repository;

import static com.example.techlog.post.domain.QPost.post;
import static com.example.techlog.tag.domain.QTag.*;

import com.example.techlog.common.RestPage;
import com.example.techlog.post.domain.Post;
import com.example.techlog.post.dto.PostSimpleResponse;
import com.example.techlog.tag.domain.PostTag;
import com.example.techlog.tag.domain.QPostTag;
import com.example.techlog.user.domain.QUser;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
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
        JPAQuery<Post> query = queryFactory.selectFrom(post)
                .leftJoin(post.writer, QUser.user).fetchJoin()
                .leftJoin(post.postTags, QPostTag.postTag).fetchJoin()
                .where(post.isDeleted.eq(false));

        long count = queryFactory.select(post.id).from(post).where(post.isDeleted.eq(false)).fetch().size();

        List<PostSimpleResponse> results = query
                .orderBy(post.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize()).stream()
                .map(this::toSimpleResponse)
                .toList();

        return new RestPage<>(new PageImpl<>(results, pageable, count));
    }

    @Override
    public RestPage<PostSimpleResponse> searchByIds(List<Long> ids, Pageable pageable) {
        JPAQuery<Post> query = queryFactory.selectFrom(post)
                .leftJoin(post.postTags, QPostTag.postTag).fetchJoin()
                .where(post.id.in(ids).and(post.isDeleted).eq(false));

        long size = query.fetch().size();

        List<PostSimpleResponse> result = query.orderBy(post.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize()).stream()
                .map(this::toSimpleResponse)
                .toList();

        return new RestPage<>(new PageImpl<>(result, pageable, size));

    }

    private PostSimpleResponse toSimpleResponse(Post post) {
        return new PostSimpleResponse(
                post.getId(),
                post.getTitle(),
                post.getThumbnail(),
                post.getWriter().getName(),
                post.getCreatedDate().toLocalDate().toString(),
                getTagList(post.getPostTags())
        );
    }

    private List<String> getTagList(List<PostTag> postTags) {

        List<Long> ids = postTags.stream()
                .map(it -> it.getTag().getId())
                .toList();

        return queryFactory.select(tag.content)
                .from(tag)
                .where(tag.id.in(ids))
                .fetch();
    }


}
