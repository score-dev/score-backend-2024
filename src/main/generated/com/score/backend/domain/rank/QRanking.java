package com.score.backend.domain.rank;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QRanking is a Querydsl query type for Ranking
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QRanking extends EntityPathBase<Ranking> {

    private static final long serialVersionUID = -712997797L;

    public static final QRanking ranking = new QRanking("ranking");

    public final DatePath<java.time.LocalDate> endDate = createDate("endDate", java.time.LocalDate.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final ListPath<Ranker, QRanker> rankers = this.<Ranker, QRanker>createList("rankers", Ranker.class, QRanker.class, PathInits.DIRECT2);

    public final DatePath<java.time.LocalDate> startDate = createDate("startDate", java.time.LocalDate.class);

    public QRanking(String variable) {
        super(Ranking.class, forVariable(variable));
    }

    public QRanking(Path<? extends Ranking> path) {
        super(path.getType(), path.getMetadata());
    }

    public QRanking(PathMetadata metadata) {
        super(Ranking.class, metadata);
    }

}

