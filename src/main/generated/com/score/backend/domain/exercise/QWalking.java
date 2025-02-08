package com.score.backend.domain.exercise;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QWalking is a Querydsl query type for Walking
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QWalking extends EntityPathBase<Walking> {

    private static final long serialVersionUID = -230724662L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QWalking walking = new QWalking("walking");

    public final QExercise _super;

    // inherited
    public final com.score.backend.domain.user.QUser agent;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> completedAt;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt;

    //inherited
    public final StringPath createdBy;

    public final NumberPath<Double> distance = createNumber("distance", Double.class);

    //inherited
    public final ListPath<com.score.backend.domain.exercise.emotion.Emotion, com.score.backend.domain.exercise.emotion.QEmotion> emotions;

    //inherited
    public final StringPath exercisePic;

    //inherited
    public final StringPath feeling;

    //inherited
    public final StringPath fineDust;

    //inherited
    public final NumberPath<Long> id;

    //inherited
    public final StringPath lastModifiedBy;

    //inherited
    public final StringPath location;

    //inherited
    public final NumberPath<Integer> reducedKcal;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> startedAt;

    //inherited
    public final ListPath<com.score.backend.domain.user.User, com.score.backend.domain.user.QUser> taggedUsers;

    //inherited
    public final NumberPath<Integer> temperature;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt;

    //inherited
    public final StringPath weather;

    public QWalking(String variable) {
        this(Walking.class, forVariable(variable), INITS);
    }

    public QWalking(Path<? extends Walking> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QWalking(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QWalking(PathMetadata metadata, PathInits inits) {
        this(Walking.class, metadata, inits);
    }

    public QWalking(Class<? extends Walking> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this._super = new QExercise(type, metadata, inits);
        this.agent = _super.agent;
        this.completedAt = _super.completedAt;
        this.createdAt = _super.createdAt;
        this.createdBy = _super.createdBy;
        this.emotions = _super.emotions;
        this.exercisePic = _super.exercisePic;
        this.feeling = _super.feeling;
        this.fineDust = _super.fineDust;
        this.id = _super.id;
        this.lastModifiedBy = _super.lastModifiedBy;
        this.location = _super.location;
        this.reducedKcal = _super.reducedKcal;
        this.startedAt = _super.startedAt;
        this.taggedUsers = _super.taggedUsers;
        this.temperature = _super.temperature;
        this.updatedAt = _super.updatedAt;
        this.weather = _super.weather;
    }

}

