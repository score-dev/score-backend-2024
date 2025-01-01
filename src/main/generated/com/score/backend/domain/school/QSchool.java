package com.score.backend.domain.school;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QSchool is a Querydsl query type for School
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSchool extends EntityPathBase<School> {

    private static final long serialVersionUID = -1508059097L;

    public static final QSchool school = new QSchool("school");

    public final com.score.backend.config.QBaseEntity _super = new com.score.backend.config.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final StringPath createdBy = _super.createdBy;

    public final ListPath<com.score.backend.domain.group.GroupEntity, com.score.backend.domain.group.QGroupEntity> groups = this.<com.score.backend.domain.group.GroupEntity, com.score.backend.domain.group.QGroupEntity>createList("groups", com.score.backend.domain.group.GroupEntity.class, com.score.backend.domain.group.QGroupEntity.class, PathInits.DIRECT2);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final StringPath lastModifiedBy = _super.lastModifiedBy;

    public final StringPath schoolAddress = createString("schoolAddress");

    public final StringPath schoolCode = createString("schoolCode");

    public final StringPath schoolLocation = createString("schoolLocation");

    public final StringPath schoolName = createString("schoolName");

    public final ListPath<com.score.backend.domain.rank.school.SchoolRanking, com.score.backend.domain.rank.school.QSchoolRanking> schoolRankings = this.<com.score.backend.domain.rank.school.SchoolRanking, com.score.backend.domain.rank.school.QSchoolRanking>createList("schoolRankings", com.score.backend.domain.rank.school.SchoolRanking.class, com.score.backend.domain.rank.school.QSchoolRanking.class, PathInits.DIRECT2);

    public final ListPath<com.score.backend.domain.user.User, com.score.backend.domain.user.QUser> students = this.<com.score.backend.domain.user.User, com.score.backend.domain.user.QUser>createList("students", com.score.backend.domain.user.User.class, com.score.backend.domain.user.QUser.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QSchool(String variable) {
        super(School.class, forVariable(variable));
    }

    public QSchool(Path<? extends School> path) {
        super(path.getType(), path.getMetadata());
    }

    public QSchool(PathMetadata metadata) {
        super(School.class, metadata);
    }

}

