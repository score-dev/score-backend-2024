package com.score.backend.domain.report.userreport;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUserReport is a Querydsl query type for UserReport
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUserReport extends EntityPathBase<UserReport> {

    private static final long serialVersionUID = 2104150619L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QUserReport userReport = new QUserReport("userReport");

    public final StringPath comment = createString("comment");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final EnumPath<UserReportReason> reason = createEnum("reason", UserReportReason.class);

    public final com.score.backend.domain.user.QUser reportAgent;

    public final com.score.backend.domain.user.QUser reportObject;

    public QUserReport(String variable) {
        this(UserReport.class, forVariable(variable), INITS);
    }

    public QUserReport(Path<? extends UserReport> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QUserReport(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QUserReport(PathMetadata metadata, PathInits inits) {
        this(UserReport.class, metadata, inits);
    }

    public QUserReport(Class<? extends UserReport> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.reportAgent = inits.isInitialized("reportAgent") ? new com.score.backend.domain.user.QUser(forProperty("reportAgent"), inits.get("reportAgent")) : null;
        this.reportObject = inits.isInitialized("reportObject") ? new com.score.backend.domain.user.QUser(forProperty("reportObject"), inits.get("reportObject")) : null;
    }

}

