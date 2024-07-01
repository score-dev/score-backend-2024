package com.score.backend.repositories.school;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.score.backend.models.QSchool;
import com.score.backend.models.School;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SchoolRepositoryImpl implements SchoolRepositoryCustom{
    private final JPAQueryFactory queryFactory;
    QSchool school = new QSchool("school");

    @Override
    public School findByNameAndAddress(String code) {
        return queryFactory
                .selectFrom(school)
                .where(school.schoolCode.eq(code))
                .fetchOne();
    }
}
