package com.score.backend.domain.exercise.emotion;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QEmotion is a Querydsl query type for Emotion
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QEmotion extends EntityPathBase<Emotion> {

    private static final long serialVersionUID = 466643737L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QEmotion emotion = new QEmotion("emotion");

    public final com.score.backend.config.QBaseEntity _super = new com.score.backend.config.QBaseEntity(this);

    public final com.score.backend.domain.user.QUser agent;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final StringPath createdBy = _super.createdBy;

    public final EnumPath<EmotionType> emotionType = createEnum("emotionType", EmotionType.class);

    public final com.score.backend.domain.exercise.QExercise feed;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final StringPath lastModifiedBy = _super.lastModifiedBy;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QEmotion(String variable) {
        this(Emotion.class, forVariable(variable), INITS);
    }

    public QEmotion(Path<? extends Emotion> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QEmotion(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QEmotion(PathMetadata metadata, PathInits inits) {
        this(Emotion.class, metadata, inits);
    }

    public QEmotion(Class<? extends Emotion> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.agent = inits.isInitialized("agent") ? new com.score.backend.domain.user.QUser(forProperty("agent"), inits.get("agent")) : null;
        this.feed = inits.isInitialized("feed") ? new com.score.backend.domain.exercise.QExercise(forProperty("feed"), inits.get("feed")) : null;
    }

}

