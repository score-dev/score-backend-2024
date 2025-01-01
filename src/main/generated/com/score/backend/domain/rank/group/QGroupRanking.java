package com.score.backend.domain.rank.group;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QGroupRanking is a Querydsl query type for GroupRanking
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QGroupRanking extends EntityPathBase<GroupRanking> {

    private static final long serialVersionUID = -1838697855L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QGroupRanking groupRanking = new QGroupRanking("groupRanking");

    public final com.score.backend.domain.rank.QRanking _super = new com.score.backend.domain.rank.QRanking(this);

    //inherited
    public final DatePath<java.time.LocalDate> endDate = _super.endDate;

    public final com.score.backend.domain.group.QGroupEntity group;

    public final ListPath<GroupRanker, QGroupRanker> groupRankers = this.<GroupRanker, QGroupRanker>createList("groupRankers", GroupRanker.class, QGroupRanker.class, PathInits.DIRECT2);

    //inherited
    public final NumberPath<Long> id = _super.id;

    //inherited
    public final DatePath<java.time.LocalDate> startDate = _super.startDate;

    public QGroupRanking(String variable) {
        this(GroupRanking.class, forVariable(variable), INITS);
    }

    public QGroupRanking(Path<? extends GroupRanking> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QGroupRanking(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QGroupRanking(PathMetadata metadata, PathInits inits) {
        this(GroupRanking.class, metadata, inits);
    }

    public QGroupRanking(Class<? extends GroupRanking> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.group = inits.isInitialized("group") ? new com.score.backend.domain.group.QGroupEntity(forProperty("group"), inits.get("group")) : null;
    }

}

