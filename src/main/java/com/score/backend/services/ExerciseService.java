package com.score.backend.services;

import com.score.backend.models.exercise.Exercise;
import com.score.backend.repositories.ExerciseRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExerciseService {
    private final ExerciseRepository exerciseRepository;

    @Transactional
    public void saveFeed(Exercise exercise) {
        exerciseRepository.save(exercise);
    }
}
