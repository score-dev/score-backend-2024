package com.score.backend.repositories.school;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.score.backend.models.QSchool;
import com.score.backend.models.School;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class SchoolRepositoryImpl implements SchoolRepositoryCustom{
    private final JPAQueryFactory queryFactory;
    QSchool school = new QSchool("school");

    @Override
    public List<School> findByNameAndAddress(String name, String address) {
        return queryFactory
                .selectFrom(school)
                .where(school.schoolName.eq(name)
                        .and(school.schoolAddress.eq(address)))
                .fetch();
    }
}
