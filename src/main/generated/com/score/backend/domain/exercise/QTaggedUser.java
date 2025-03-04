package com.score.backend.domain.exercise;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QTaggedUser is a Querydsl query type for TaggedUser
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTaggedUser extends EntityPathBase<TaggedUser> {

    private static final long serialVersionUID = -484530330L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QTaggedUser taggedUser = new QTaggedUser("taggedUser");

    public final QExercise exercise;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.score.backend.domain.user.QUser user;

    public QTaggedUser(String variable) {
        this(TaggedUser.class, forVariable(variable), INITS);
    }

    public QTaggedUser(Path<? extends TaggedUser> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QTaggedUser(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QTaggedUser(PathMetadata metadata, PathInits inits) {
        this(TaggedUser.class, metadata, inits);
    }

    public QTaggedUser(Class<? extends TaggedUser> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.exercise = inits.isInitialized("exercise") ? new QExercise(forProperty("exercise"), inits.get("exercise")) : null;
        this.user = inits.isInitialized("user") ? new com.score.backend.domain.user.QUser(forProperty("user"), inits.get("user")) : null;
    }

}

