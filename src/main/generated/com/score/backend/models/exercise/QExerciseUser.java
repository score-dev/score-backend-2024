package com.score.backend.models.exercise;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QExerciseUser is a Querydsl query type for ExerciseUser
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QExerciseUser extends EntityPathBase<ExerciseUser> {

    private static final long serialVersionUID = 1126731576L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QExerciseUser exerciseUser = new QExerciseUser("exerciseUser");

    public final QExercise exercise;

    public final com.score.backend.models.QUser user;

    public QExerciseUser(String variable) {
        this(ExerciseUser.class, forVariable(variable), INITS);
    }

    public QExerciseUser(Path<? extends ExerciseUser> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QExerciseUser(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QExerciseUser(PathMetadata metadata, PathInits inits) {
        this(ExerciseUser.class, metadata, inits);
    }

    public QExerciseUser(Class<? extends ExerciseUser> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.exercise = inits.isInitialized("exercise") ? new QExercise(forProperty("exercise"), inits.get("exercise")) : null;
        this.user = inits.isInitialized("user") ? new com.score.backend.models.QUser(forProperty("user"), inits.get("user")) : null;
    }

}

