package com.score.backend.repositories.exercise;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.score.backend.models.exercise.Exercise;
import com.score.backend.models.exercise.QExercise;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

@RequiredArgsConstructor
public class ExerciseRepositoryImpl implements ExerciseRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    QExercise e = new QExercise("e");

    @Override
    public List<Exercise> findUsersExerciseToday(Long userId, LocalDateTime today) {
        return queryFactory
                .selectFrom(e)
                .where(userIdEq(userId), completeDateEq(today.truncatedTo(ChronoUnit.SECONDS)))
                .fetch();
    }

    private BooleanExpression userIdEq(Long userIdCond) {
        return userIdCond != null ? e.agent.id.eq(userIdCond) : null;
    }

    private BooleanExpression completeDateEq(LocalDateTime dateCond) {
        String formattedTime = dateCond.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        return Expressions.stringTemplate("TO_CHAR(DATE_TRUNC('second', {0}), 'YYYY-MM-DD\"T\"HH24:MI:SS')", e.completedAt)
                .eq(formattedTime);
    }
}
