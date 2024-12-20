package com.score.backend.domain.exercise.repositories;

import com.score.backend.domain.exercise.Exercise;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface ExerciseRepositoryCustom {
    List<Exercise> findUsersExerciseToday(Long userId, LocalDateTime today);
    List<Exercise> findByUserId(Long userId);
    Page<Exercise> findExercisePageByUserId(Long userId, Pageable pageable);
    Page<Exercise> findExercisePageByGroupId(Long groupId, Pageable pageable);
    Page<String> findFeedsImgPageByGroupId(Long groupId, Pageable pageable);
}
