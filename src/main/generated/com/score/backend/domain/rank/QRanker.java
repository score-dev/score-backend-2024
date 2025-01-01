package com.score.backend.domain.rank;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QRanker is a Querydsl query type for Ranker
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QRanker extends EntityPathBase<Ranker> {

    private static final long serialVersionUID = 254094612L;

    public static final QRanker ranker = new QRanker("ranker");

    public final NumberPath<Integer> changedAmount = createNumber("changedAmount", Integer.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Integer> rankNum = createNumber("rankNum", Integer.class);

    public QRanker(String variable) {
        super(Ranker.class, forVariable(variable));
    }

    public QRanker(Path<? extends Ranker> path) {
        super(path.getType(), path.getMetadata());
    }

    public QRanker(PathMetadata metadata) {
        super(Ranker.class, metadata);
    }

}

