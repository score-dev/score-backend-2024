package com.score.backend.services;

import com.score.backend.models.exercise.Exercise;
import com.score.backend.repositories.ExerciseRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ExerciseService {
    private final ExerciseRepository exerciseRepository;

    @Transactional
    public void saveFeed(Exercise exercise) {
        exerciseRepository.save(exercise);
    }

    @Transactional
    public void deleteFeed(Exercise exercise) {
        exerciseRepository.delete(exercise);
    }

    public Optional<Exercise> findFeedByExerciseId(Long exerciseId) {
        return exerciseRepository.findById(exerciseId);
    }

    // 운동한 시간 계산
    public double calculateExerciseDuration(LocalDateTime start, LocalDateTime end) {
        Duration duration = Duration.between(start, end);
        return duration.getSeconds();
    }

}
