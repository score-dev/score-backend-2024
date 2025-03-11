package com.score.backend.domain.report.userreport.repositories;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.score.backend.domain.report.userreport.QUserReport;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserReportRepositoryImpl implements UserReportRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    QUserReport userReport = QUserReport.userReport;

    @Override
    public int findDistinctReportCounts(Long userId) {
        return Math.toIntExact(
                queryFactory
                        .select(userReport.reportAgent.countDistinct()).from(userReport)
                        .where(userReport.reportObject.id.eq(userId))
                        .fetchFirst()
        );
    }
}
