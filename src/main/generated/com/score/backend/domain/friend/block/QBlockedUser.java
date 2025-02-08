package com.score.backend.domain.friend.block;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QBlockedUser is a Querydsl query type for BlockedUser
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBlockedUser extends EntityPathBase<BlockedUser> {

    private static final long serialVersionUID = -1510702579L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QBlockedUser blockedUser = new QBlockedUser("blockedUser");

    public final com.score.backend.config.QBaseEntity _super = new com.score.backend.config.QBaseEntity(this);

    public final com.score.backend.domain.user.QUser blocked;

    public final com.score.backend.domain.user.QUser blocker;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final StringPath createdBy = _super.createdBy;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final StringPath lastModifiedBy = _super.lastModifiedBy;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QBlockedUser(String variable) {
        this(BlockedUser.class, forVariable(variable), INITS);
    }

    public QBlockedUser(Path<? extends BlockedUser> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QBlockedUser(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QBlockedUser(PathMetadata metadata, PathInits inits) {
        this(BlockedUser.class, metadata, inits);
    }

    public QBlockedUser(Class<? extends BlockedUser> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.blocked = inits.isInitialized("blocked") ? new com.score.backend.domain.user.QUser(forProperty("blocked"), inits.get("blocked")) : null;
        this.blocker = inits.isInitialized("blocker") ? new com.score.backend.domain.user.QUser(forProperty("blocker"), inits.get("blocker")) : null;
    }

}

