package com.score.backend.repositories.exercise;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ExerciseRepositoryCustomImpl implements ExerciseRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;
}
