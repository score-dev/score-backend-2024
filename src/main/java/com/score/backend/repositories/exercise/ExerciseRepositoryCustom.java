package com.score.backend.repositories.exercise;

import com.score.backend.models.exercise.Exercise;

import java.time.LocalDateTime;
import java.util.List;

public interface ExerciseRepositoryCustom {
    List<Exercise> findUsersExerciseToday(Long userId, LocalDateTime today);

}
