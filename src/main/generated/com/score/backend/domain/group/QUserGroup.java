package com.score.backend.domain.group;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUserGroup is a Querydsl query type for UserGroup
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUserGroup extends EntityPathBase<UserGroup> {

    private static final long serialVersionUID = -1038821570L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QUserGroup userGroup = new QUserGroup("userGroup");

    public final com.score.backend.config.QBaseEntity _super = new com.score.backend.config.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final StringPath createdBy = _super.createdBy;

    public final QGroupEntity group;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final DateTimePath<java.time.LocalDateTime> joinedAt = createDateTime("joinedAt", java.time.LocalDateTime.class);

    //inherited
    public final StringPath lastModifiedBy = _super.lastModifiedBy;

    public final com.score.backend.domain.user.QUser member;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QUserGroup(String variable) {
        this(UserGroup.class, forVariable(variable), INITS);
    }

    public QUserGroup(Path<? extends UserGroup> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QUserGroup(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QUserGroup(PathMetadata metadata, PathInits inits) {
        this(UserGroup.class, metadata, inits);
    }

    public QUserGroup(Class<? extends UserGroup> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.group = inits.isInitialized("group") ? new QGroupEntity(forProperty("group"), inits.get("group")) : null;
        this.member = inits.isInitialized("member") ? new com.score.backend.domain.user.QUser(forProperty("member"), inits.get("member")) : null;
    }

}

