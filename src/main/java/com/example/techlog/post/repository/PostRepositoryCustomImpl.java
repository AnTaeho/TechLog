package com.example.techlog.post.repository;

import static com.example.techlog.post.domain.QPost.post;
import static com.example.techlog.tag.domain.QTag.*;

import com.example.techlog.common.RestPage;
import com.example.techlog.post.domain.Post;
import com.example.techlog.post.dto.PostSimpleResponse;
import com.example.techlog.tag.domain.PostTag;
import com.example.techlog.tag.domain.QPostTag;
import com.example.techlog.tag.domain.QTag;
import com.example.techlog.user.domain.QUser;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Collections;
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
    public RestPage<PostSimpleResponse> getPostPageWithWriterPage(Pageable pageable) {
//        JPAQuery<Post> query = queryFactory.selectFrom(post)
//                .leftJoin(post.writer, QUser.user).fetchJoin()
//                .where(post.isDeleted.eq(false))
//                .orderBy(post.id.desc())
//                .offset(pageable.getOffset())
//                .limit(pageable.getPageSize());
//
//        long count = queryFactory.select(post.id)
//                .from(post)
//                .where(post.isDeleted.eq(false))
//                .fetch().size();
//
//        List<PostSimpleResponse> result = query.stream()
//                .map(this::toSimpleResponse)
//                .toList();
//
//        return new RestPage<>(new PageImpl<>(result, pageable, count));

        List<Long> postIds = queryFactory.select(post.id)
                .from(post)
                .where(post.isDeleted.eq(false))
                .orderBy(post.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        if (postIds.isEmpty()) {
            return new RestPage<>(new PageImpl<>(Collections.emptyList(), pageable, 0));
        }

        // Step 2: 해당 Post ID들을 이용해 연관된 엔티티를 페치 조인으로 조회
        List<Post> posts = queryFactory.selectFrom(post)
                .leftJoin(post.writer, QUser.user).fetchJoin()
                .leftJoin(post.postTags, QPostTag.postTag).fetchJoin()
                .leftJoin(QPostTag.postTag.tag, QTag.tag).fetchJoin()
                .where(post.id.in(postIds))
                .orderBy(post.id.desc())
                .fetch();

        // Step 3: 전체 Post의 개수 계산
        long count = queryFactory.select(post.id)
                .from(post)
                .where(post.isDeleted.eq(false))
                .fetch().size();

        // Step 4: 결과 변환
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

    private List<String> getTagList(List<PostTag> postTags) {

        List<Long> ids = postTags.stream()
                .map(it -> it.getTag().getId())
                .toList();

        return queryFactory.select(tag.content)
                .from(tag)
                .where(tag.id.in(ids))
                .fetch();
    }

    @Override
    public RestPage<PostSimpleResponse> searchByIds(List<Long> ids, Pageable pageable) {
        JPAQuery<Post> query = queryFactory.selectFrom(post)
//                .leftJoin(post.postTags, QPostTag.postTag).fetchJoin()
                .where(post.id.in(ids))
                .where(post.isDeleted.eq(false));

        long total = queryFactory
                .select(post.id)
                .from(post)
                .where(post.isDeleted.eq(false))
                .where(post.id.in(ids))
                .fetch().size();

        List<PostSimpleResponse> result = query
                .orderBy(post.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize()).fetch()
                .stream()
                .map(this::toSimpleResponse)
//                .distinct()
                .toList();

        return new RestPage<>(new PageImpl<>(result, pageable, total));
    }

}
