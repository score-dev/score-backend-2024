package com.score.backend.domain.rank.school;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QSchoolRanking is a Querydsl query type for SchoolRanking
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSchoolRanking extends EntityPathBase<SchoolRanking> {

    private static final long serialVersionUID = -1691623197L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSchoolRanking schoolRanking = new QSchoolRanking("schoolRanking");

    public final com.score.backend.domain.rank.QRanking _super = new com.score.backend.domain.rank.QRanking(this);

    //inherited
    public final DatePath<java.time.LocalDate> endDate = _super.endDate;

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final com.score.backend.domain.school.QSchool school;

    public final ListPath<SchoolRanker, QSchoolRanker> schoolRankers = this.<SchoolRanker, QSchoolRanker>createList("schoolRankers", SchoolRanker.class, QSchoolRanker.class, PathInits.DIRECT2);

    //inherited
    public final DatePath<java.time.LocalDate> startDate = _super.startDate;

    public QSchoolRanking(String variable) {
        this(SchoolRanking.class, forVariable(variable), INITS);
    }

    public QSchoolRanking(Path<? extends SchoolRanking> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QSchoolRanking(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QSchoolRanking(PathMetadata metadata, PathInits inits) {
        this(SchoolRanking.class, metadata, inits);
    }

    public QSchoolRanking(Class<? extends SchoolRanking> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.school = inits.isInitialized("school") ? new com.score.backend.domain.school.QSchool(forProperty("school")) : null;
    }

}

