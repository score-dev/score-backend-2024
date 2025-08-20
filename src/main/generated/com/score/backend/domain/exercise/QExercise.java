package com.score.backend.domain.exercise;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QExercise is a Querydsl query type for Exercise
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QExercise extends EntityPathBase<Exercise> {

    private static final long serialVersionUID = -1124719193L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QExercise exercise = new QExercise("exercise");

    public final com.score.backend.config.QBaseEntity _super = new com.score.backend.config.QBaseEntity(this);

    public final com.score.backend.domain.user.QUser agent;

    public final DateTimePath<java.time.LocalDateTime> completedAt = createDateTime("completedAt", java.time.LocalDateTime.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final StringPath createdBy = _super.createdBy;

    public final NumberPath<Double> durationSec = createNumber("durationSec", Double.class);

    public final ListPath<com.score.backend.domain.exercise.emotion.Emotion, com.score.backend.domain.exercise.emotion.QEmotion> emotions = this.<com.score.backend.domain.exercise.emotion.Emotion, com.score.backend.domain.exercise.emotion.QEmotion>createList("emotions", com.score.backend.domain.exercise.emotion.Emotion.class, com.score.backend.domain.exercise.emotion.QEmotion.class, PathInits.DIRECT2);

    public final StringPath exercisePic = createString("exercisePic");

    public final StringPath feeling = createString("feeling");

    public final StringPath fineDust = createString("fineDust");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final StringPath lastModifiedBy = _super.lastModifiedBy;

    public final StringPath location = createString("location");

    public final NumberPath<Integer> reducedKcal = createNumber("reducedKcal", Integer.class);

    public final DateTimePath<java.time.LocalDateTime> startedAt = createDateTime("startedAt", java.time.LocalDateTime.class);

    public final ListPath<TaggedUser, QTaggedUser> taggedUsers = this.<TaggedUser, QTaggedUser>createList("taggedUsers", TaggedUser.class, QTaggedUser.class, PathInits.DIRECT2);

    public final NumberPath<Integer> temperature = createNumber("temperature", Integer.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final StringPath weather = createString("weather");

    public QExercise(String variable) {
        this(Exercise.class, forVariable(variable), INITS);
    }

    public QExercise(Path<? extends Exercise> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QExercise(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QExercise(PathMetadata metadata, PathInits inits) {
        this(Exercise.class, metadata, inits);
    }

    public QExercise(Class<? extends Exercise> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.agent = inits.isInitialized("agent") ? new com.score.backend.domain.user.QUser(forProperty("agent"), inits.get("agent")) : null;
    }

}

