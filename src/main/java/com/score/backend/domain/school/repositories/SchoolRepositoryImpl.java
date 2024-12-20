package com.score.backend.domain.school.repositories;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.score.backend.domain.school.QSchool;
import com.score.backend.domain.school.School;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SchoolRepositoryImpl implements SchoolRepositoryCustom{
    private final JPAQueryFactory queryFactory;
    QSchool school = new QSchool("school");

    @Override
    public School findSchoolByCode(String code) {
        return queryFactory
                .selectFrom(school)
                .where(school.schoolCode.eq(code))
                .fetchOne();
    }
}
