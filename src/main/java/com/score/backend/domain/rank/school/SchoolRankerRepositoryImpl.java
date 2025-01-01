package com.score.backend.domain.rank.school;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.score.backend.domain.group.QGroupEntity;
import com.score.backend.domain.rank.QRanker;
import com.score.backend.domain.rank.Ranker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
@RequiredArgsConstructor
public class SchoolRankerRepositoryImpl implements SchoolRankerRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    QSchoolRanking schoolRanking = QSchoolRanking.schoolRanking;
    QRanker ranker = QRanker.ranker;
    QSchoolRanker schoolRanker = QSchoolRanker.schoolRanker;
    QGroupEntity groupEntity = QGroupEntity.groupEntity;

    @Override
    public SchoolRanker findLastWeekSchoolRankerByGroupId(Long groupId) {
        return (SchoolRanker) queryFactory.select(ranker)
                .from(schoolRanking)
                .join(schoolRanking.rankers, ranker)
                .join(schoolRanker).on(ranker.id.eq(schoolRanker.id))
                .join(schoolRanker.group, groupEntity)
                .where(
                        schoolRanking.startDate.eq(LocalDate.now().minusDays(14)),
                        groupEntity.groupId.eq(groupId)
                ).fetchOne();
    }
}
