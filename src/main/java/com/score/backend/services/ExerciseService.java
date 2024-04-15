package com.score.backend.services;

import com.score.backend.models.exercise.Exercise;
import com.score.backend.repositories.ExerciseRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
}
