package com.score.backend.domain.user;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUser is a Querydsl query type for User
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUser extends EntityPathBase<User> {

    private static final long serialVersionUID = 1164835207L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QUser user = new QUser("user");

    public final com.score.backend.config.QBaseEntity _super = new com.score.backend.config.QBaseEntity(this);

    public final ListPath<com.score.backend.domain.friend.block.BlockedUser, com.score.backend.domain.friend.block.QBlockedUser> blockedUsers = this.<com.score.backend.domain.friend.block.BlockedUser, com.score.backend.domain.friend.block.QBlockedUser>createList("blockedUsers", com.score.backend.domain.friend.block.BlockedUser.class, com.score.backend.domain.friend.block.QBlockedUser.class, PathInits.DIRECT2);

    public final NumberPath<Integer> consecutiveDate = createNumber("consecutiveDate", Integer.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final StringPath createdBy = _super.createdBy;

    public final NumberPath<Double> cumulativeDistance = createNumber("cumulativeDistance", Double.class);

    public final BooleanPath exercisingTime = createBoolean("exercisingTime");

    public final StringPath fcmToken = createString("fcmToken");

    public final ListPath<com.score.backend.domain.exercise.Exercise, com.score.backend.domain.exercise.QExercise> feeds = this.<com.score.backend.domain.exercise.Exercise, com.score.backend.domain.exercise.QExercise>createList("feeds", com.score.backend.domain.exercise.Exercise.class, com.score.backend.domain.exercise.QExercise.class, PathInits.DIRECT2);

    public final ListPath<com.score.backend.domain.friend.Friend, com.score.backend.domain.friend.QFriend> friends = this.<com.score.backend.domain.friend.Friend, com.score.backend.domain.friend.QFriend>createList("friends", com.score.backend.domain.friend.Friend.class, com.score.backend.domain.friend.QFriend.class, PathInits.DIRECT2);

    public final EnumPath<Gender> gender = createEnum("gender", Gender.class);

    public final TimePath<java.time.LocalTime> goal = createTime("goal", java.time.LocalTime.class);

    public final NumberPath<Integer> grade = createNumber("grade", Integer.class);

    public final NumberPath<Integer> height = createNumber("height", Integer.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final DateTimePath<java.time.LocalDateTime> joinedAt = createDateTime("joinedAt", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> lastExerciseDateTime = createDateTime("lastExerciseDateTime", java.time.LocalDateTime.class);

    //inherited
    public final StringPath lastModifiedBy = _super.lastModifiedBy;

    public final NumberPath<Integer> level = createNumber("level", Integer.class);

    public final StringPath loginKey = createString("loginKey");

    public final BooleanPath marketing = createBoolean("marketing");

    public final StringPath nickname = createString("nickname");

    public final ListPath<com.score.backend.domain.notification.Notification, com.score.backend.domain.notification.QNotification> notifications = this.<com.score.backend.domain.notification.Notification, com.score.backend.domain.notification.QNotification>createList("notifications", com.score.backend.domain.notification.Notification.class, com.score.backend.domain.notification.QNotification.class, PathInits.DIRECT2);

    public final NumberPath<Integer> point = createNumber("point", Integer.class);

    public final StringPath profileImg = createString("profileImg");

    public final StringPath refreshToken = createString("refreshToken");

    public final com.score.backend.domain.school.QSchool school;

    public final DateTimePath<java.time.LocalDateTime> schoolUpdatedAt = createDateTime("schoolUpdatedAt", java.time.LocalDateTime.class);

    public final BooleanPath tag = createBoolean("tag");

    public final NumberPath<Double> totalCumulativeTime = createNumber("totalCumulativeTime", Double.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final ListPath<com.score.backend.domain.group.UserGroup, com.score.backend.domain.group.QUserGroup> userGroups = this.<com.score.backend.domain.group.UserGroup, com.score.backend.domain.group.QUserGroup>createList("userGroups", com.score.backend.domain.group.UserGroup.class, com.score.backend.domain.group.QUserGroup.class, PathInits.DIRECT2);

    public final NumberPath<Double> weeklyCumulativeTime = createNumber("weeklyCumulativeTime", Double.class);

    public final NumberPath<Integer> weeklyExerciseCount = createNumber("weeklyExerciseCount", Integer.class);

    public final NumberPath<Integer> weeklyLevelIncrement = createNumber("weeklyLevelIncrement", Integer.class);

    public final NumberPath<Integer> weight = createNumber("weight", Integer.class);

    public QUser(String variable) {
        this(User.class, forVariable(variable), INITS);
    }

    public QUser(Path<? extends User> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QUser(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QUser(PathMetadata metadata, PathInits inits) {
        this(User.class, metadata, inits);
    }

    public QUser(Class<? extends User> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.school = inits.isInitialized("school") ? new com.score.backend.domain.school.QSchool(forProperty("school")) : null;
    }

}

