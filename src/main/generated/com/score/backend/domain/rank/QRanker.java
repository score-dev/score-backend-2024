package com.score.backend.domain.rank;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QRanker is a Querydsl query type for Ranker
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QRanker extends EntityPathBase<Ranker> {

    private static final long serialVersionUID = 254094612L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QRanker ranker = new QRanker("ranker");

    public final QRanking belongsTo;

    public final NumberPath<Integer> changedAmount = createNumber("changedAmount", Integer.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Integer> rankNum = createNumber("rankNum", Integer.class);

    public QRanker(String variable) {
        this(Ranker.class, forVariable(variable), INITS);
    }

    public QRanker(Path<? extends Ranker> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QRanker(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QRanker(PathMetadata metadata, PathInits inits) {
        this(Ranker.class, metadata, inits);
    }

    public QRanker(Class<? extends Ranker> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.belongsTo = inits.isInitialized("belongsTo") ? new QRanking(forProperty("belongsTo")) : null;
    }

}

