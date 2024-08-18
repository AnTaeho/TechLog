package com.example.techlog.user.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUser is a Querydsl query type for User
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUser extends EntityPathBase<User> {

    private static final long serialVersionUID = -681983741L;

    public static final QUser user = new QUser("user");

    public final com.example.techlog.common.entity.QBaseEntity _super = new com.example.techlog.common.entity.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final StringPath email = createString("email");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isDeleted = createBoolean("isDeleted");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedDate = _super.lastModifiedDate;

    public final StringPath name = createString("name");

    public final StringPath password = createString("password");

    public final ListPath<com.example.techlog.post.domain.Post, com.example.techlog.post.domain.QPost> posts = this.<com.example.techlog.post.domain.Post, com.example.techlog.post.domain.QPost>createList("posts", com.example.techlog.post.domain.Post.class, com.example.techlog.post.domain.QPost.class, PathInits.DIRECT2);

    public final EnumPath<UserRole> role = createEnum("role", UserRole.class);

    public final ListPath<com.example.techlog.tag.domain.Tag, com.example.techlog.tag.domain.QTag> tags = this.<com.example.techlog.tag.domain.Tag, com.example.techlog.tag.domain.QTag>createList("tags", com.example.techlog.tag.domain.Tag.class, com.example.techlog.tag.domain.QTag.class, PathInits.DIRECT2);

    public QUser(String variable) {
        super(User.class, forVariable(variable));
    }

    public QUser(Path<? extends User> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUser(PathMetadata metadata) {
        super(User.class, metadata);
    }

}

