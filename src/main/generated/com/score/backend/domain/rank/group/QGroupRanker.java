package com.score.backend.domain.rank.group;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QGroupRanker is a Querydsl query type for GroupRanker
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QGroupRanker extends EntityPathBase<GroupRanker> {

    private static final long serialVersionUID = -752049618L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QGroupRanker groupRanker = new QGroupRanker("groupRanker");

    public final com.score.backend.domain.rank.QRanker _super;

    // inherited
    public final com.score.backend.domain.rank.QRanking belongsTo;

    //inherited
    public final NumberPath<Integer> changedAmount;

    public final NumberPath<Integer> changedDegree = createNumber("changedDegree", Integer.class);

    //inherited
    public final NumberPath<Long> id;

    //inherited
    public final NumberPath<Integer> rankNum;

    public final com.score.backend.domain.user.QUser user;

    public final NumberPath<Double> weeklyExerciseTime = createNumber("weeklyExerciseTime", Double.class);

    public final NumberPath<Integer> weeklyLevelIncrement = createNumber("weeklyLevelIncrement", Integer.class);

    public QGroupRanker(String variable) {
        this(GroupRanker.class, forVariable(variable), INITS);
    }

    public QGroupRanker(Path<? extends GroupRanker> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QGroupRanker(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QGroupRanker(PathMetadata metadata, PathInits inits) {
        this(GroupRanker.class, metadata, inits);
    }

    public QGroupRanker(Class<? extends GroupRanker> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this._super = new com.score.backend.domain.rank.QRanker(type, metadata, inits);
        this.belongsTo = _super.belongsTo;
        this.changedAmount = _super.changedAmount;
        this.id = _super.id;
        this.rankNum = _super.rankNum;
        this.user = inits.isInitialized("user") ? new com.score.backend.domain.user.QUser(forProperty("user"), inits.get("user")) : null;
    }

}

