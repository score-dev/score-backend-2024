package com.score.backend.domain.rank.school;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.score.backend.domain.school.QSchool;
import com.score.backend.domain.user.QUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class SchoolRankingRepositoryImpl implements SchoolRankingRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    QUser user = QUser.user;
    QSchool school = QSchool.school;
    QSchoolRanking schoolRanking = QSchoolRanking.schoolRanking;

    @Override
    public SchoolRanking findSchoolRankingByUserIdAndStartDate(Long userId, LocalDate startDate) {
        Optional<Long> schoolId = Optional.ofNullable(
                queryFactory.select(school.id)
                        .from(user)
                        .join(user.school, school)
                        .where(user.id.eq(userId))
                        .fetchOne()
        );

        return schoolId.map(id ->
                queryFactory.selectFrom(schoolRanking)
                        .join(schoolRanking.school, school)
                        .where(
                                schoolRanking.startDate.eq(startDate),
                                school.id.eq(id)
                        )
                        .fetchOne()
        ).orElseThrow(() -> new IllegalArgumentException("해당 User가 속한 School을 찾을 수 없습니다."));
    }
}
