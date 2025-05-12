package com.score.backend.domain.notification;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QNotification is a Querydsl query type for Notification
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QNotification extends EntityPathBase<Notification> {

    private static final long serialVersionUID = -1100912505L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QNotification notification = new QNotification("notification");

    public final com.score.backend.config.QBaseEntity _super = new com.score.backend.config.QBaseEntity(this);

    public final StringPath body = createString("body");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final StringPath createdBy = _super.createdBy;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isJoinRequestAccepted = createBoolean("isJoinRequestAccepted");

    public final BooleanPath isRead = createBoolean("isRead");

    //inherited
    public final StringPath lastModifiedBy = _super.lastModifiedBy;

    public final com.score.backend.domain.user.QUser receiver;

    public final com.score.backend.domain.exercise.QExercise relatedFeed;

    public final com.score.backend.domain.group.QGroupEntity relatedGroup;

    public final com.score.backend.domain.user.QUser sender;

    public final StringPath title = createString("title");

    public final EnumPath<NotificationType> type = createEnum("type", NotificationType.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QNotification(String variable) {
        this(Notification.class, forVariable(variable), INITS);
    }

    public QNotification(Path<? extends Notification> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QNotification(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QNotification(PathMetadata metadata, PathInits inits) {
        this(Notification.class, metadata, inits);
    }

    public QNotification(Class<? extends Notification> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.receiver = inits.isInitialized("receiver") ? new com.score.backend.domain.user.QUser(forProperty("receiver"), inits.get("receiver")) : null;
        this.relatedFeed = inits.isInitialized("relatedFeed") ? new com.score.backend.domain.exercise.QExercise(forProperty("relatedFeed"), inits.get("relatedFeed")) : null;
        this.relatedGroup = inits.isInitialized("relatedGroup") ? new com.score.backend.domain.group.QGroupEntity(forProperty("relatedGroup"), inits.get("relatedGroup")) : null;
        this.sender = inits.isInitialized("sender") ? new com.score.backend.domain.user.QUser(forProperty("sender"), inits.get("sender")) : null;
    }

}

