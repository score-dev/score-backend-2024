package com.score.backend.domain.report.feedreport;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QFeedReport is a Querydsl query type for FeedReport
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QFeedReport extends EntityPathBase<FeedReport> {

    private static final long serialVersionUID = 83714171L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QFeedReport feedReport = new QFeedReport("feedReport");

    public final StringPath comment = createString("comment");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final EnumPath<FeedReportReason> reason = createEnum("reason", FeedReportReason.class);

    public final com.score.backend.domain.user.QUser reportAgent;

    public final com.score.backend.domain.exercise.QExercise reportedFeed;

    public QFeedReport(String variable) {
        this(FeedReport.class, forVariable(variable), INITS);
    }

    public QFeedReport(Path<? extends FeedReport> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QFeedReport(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QFeedReport(PathMetadata metadata, PathInits inits) {
        this(FeedReport.class, metadata, inits);
    }

    public QFeedReport(Class<? extends FeedReport> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.reportAgent = inits.isInitialized("reportAgent") ? new com.score.backend.domain.user.QUser(forProperty("reportAgent"), inits.get("reportAgent")) : null;
        this.reportedFeed = inits.isInitialized("reportedFeed") ? new com.score.backend.domain.exercise.QExercise(forProperty("reportedFeed"), inits.get("reportedFeed")) : null;
    }

}

