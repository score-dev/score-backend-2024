package com.score.backend.domain.rank.school;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.score.backend.domain.group.QGroupEntity;
import com.score.backend.domain.user.QUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class SchoolRankerRepositoryImpl implements SchoolRankerRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    QSchoolRanking schoolRanking = QSchoolRanking.schoolRanking;
    QSchoolRanker schoolRanker = QSchoolRanker.schoolRanker;
    QGroupEntity groupEntity = QGroupEntity.groupEntity;
    QUser user = QUser.user;

    @Override
    public SchoolRanker findLastWeekSchoolRankerByGroupId(Long groupId) {
        return queryFactory.select(schoolRanker)
                .from(schoolRanking)
                .join(schoolRanker).on(schoolRanker.id.eq(schoolRanker.id))
                .join(schoolRanker.group, groupEntity)
                .where(
                        schoolRanking.startDate.eq(LocalDate.now().minusDays(14)),
                        groupEntity.groupId.eq(groupId)
                ).fetchOne();
    }

    public List<SchoolRanker> findSchoolRankersByUserIdAndStartDate(Long userId, LocalDate startDate) {

        return queryFactory
                .selectFrom(schoolRanker)
                .join(schoolRanker.belongsTo, schoolRanking)
                .join(schoolRanking.school.groups, groupEntity)
                .join(groupEntity.members, user)
                .where(
                        schoolRanking.startDate.eq(startDate),
                        user.id.eq(userId)
                )
                .fetch();

    }
}
