package com.score.backend.domain.rank.group;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.score.backend.domain.group.QGroupEntity;
import com.score.backend.domain.rank.QRanker;
import com.score.backend.domain.user.QUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
@RequiredArgsConstructor
public class GroupRankerRepositoryImpl implements GroupRankerRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    QGroupRanking groupRanking = QGroupRanking.groupRanking;
    QRanker ranker = QRanker.ranker;
    QGroupRanker groupRanker = QGroupRanker.groupRanker;
    QGroupEntity groupEntity = QGroupEntity.groupEntity;
    QUser user = QUser.user;

    @Override
    public GroupRanker findLastWeekGroupRankerByGroupIdAndUserId(Long groupId, Long userId) {
        return (GroupRanker) queryFactory.select(ranker)
                .from(groupRanking)
                .join(groupRanking.rankers, ranker)
                .join(groupRanking.group, groupEntity)
                .join(groupRanker).on(ranker.id.eq(groupRanker.id))
                .join(groupRanker.user, user)
                .where(
                        groupRanking.startDate.eq(LocalDate.now().minusDays(14)),
                        groupEntity.groupId.eq(groupId),
                        user.id.eq(userId)
                )
                .fetchOne();
    }
}