package com.score.backend.domain.exercise.repositories;

import com.score.backend.domain.exercise.Exercise;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ExerciseRepositoryCustom {
    List<Exercise> findUsersWeeklyExercises(Long userId, LocalDate today);
    List<Exercise> findUsersMonthlyExercises(Long userId, int year, int month);
    int countUsersValidateExerciseToday(Long userId, LocalDate today);
    Page<Exercise> findExercisePageByUserId(Long userId, Pageable pageable);
    Page<Exercise> findExercisePageByGroupId(Long groupId, Pageable pageable);
}
