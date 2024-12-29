package com.score.backend.domain.rank.school;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QSchoolRanker is a Querydsl query type for SchoolRanker
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSchoolRanker extends EntityPathBase<SchoolRanker> {

    private static final long serialVersionUID = 1607999372L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSchoolRanker schoolRanker = new QSchoolRanker("schoolRanker");

    public final com.score.backend.domain.rank.QRanker _super;

    // inherited
    public final com.score.backend.domain.rank.QRanking belongsTo;

    //inherited
    public final NumberPath<Integer> changedAmount;

    public final com.score.backend.domain.group.QGroupEntity group;

    //inherited
    public final NumberPath<Long> id;

    public final NumberPath<Double> participateRatio = createNumber("participateRatio", Double.class);

    //inherited
    public final NumberPath<Integer> rankNum;

    public final NumberPath<Double> totalExerciseTime = createNumber("totalExerciseTime", Double.class);

    public QSchoolRanker(String variable) {
        this(SchoolRanker.class, forVariable(variable), INITS);
    }

    public QSchoolRanker(Path<? extends SchoolRanker> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QSchoolRanker(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QSchoolRanker(PathMetadata metadata, PathInits inits) {
        this(SchoolRanker.class, metadata, inits);
    }

    public QSchoolRanker(Class<? extends SchoolRanker> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this._super = new com.score.backend.domain.rank.QRanker(type, metadata, inits);
        this.belongsTo = _super.belongsTo;
        this.changedAmount = _super.changedAmount;
        this.group = inits.isInitialized("group") ? new com.score.backend.domain.group.QGroupEntity(forProperty("group"), inits.get("group")) : null;
        this.id = _super.id;
        this.rankNum = _super.rankNum;
    }

}

