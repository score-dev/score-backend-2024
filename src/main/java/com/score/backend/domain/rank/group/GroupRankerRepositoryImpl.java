package com.score.backend.domain.rank.group;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.score.backend.domain.group.QGroupEntity;
import com.score.backend.domain.user.QUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
@RequiredArgsConstructor
public class GroupRankerRepositoryImpl implements GroupRankerRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    QGroupRanking groupRanking = QGroupRanking.groupRanking;
    QGroupRanker groupRanker = QGroupRanker.groupRanker;
    QGroupEntity groupEntity = QGroupEntity.groupEntity;
    QUser user = QUser.user;

    @Override
    public GroupRanker findLastWeekGroupRankerByGroupIdAndUserId(Long groupId, Long userId) {
        return queryFactory.select(groupRanker)
                .from(groupRanking)
                .join(groupRanking.groupRankers, groupRanker)
                .join(groupRanking.group, groupEntity)
                .join(groupRanker.user, user)
                .where(
                        groupRanking.startDate.eq(LocalDate.now().minusDays(14)),
                        groupEntity.groupId.eq(groupId),
                        user.id.eq(userId)
                )
                .fetchOne();
    }
}
