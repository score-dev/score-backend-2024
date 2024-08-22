package com.score.backend.services;

import com.score.backend.models.User;
import com.score.backend.models.exercise.Exercise;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class LevelService {

    private final UserService userService;
    private final ExerciseService exerciseService;

    // 누적 운동 거리에 따른 포인트 증가
    public void increasePointsByWalkingDistance(Long userId, double newDistance) {
        User user = userService.findUserById(userId).orElseThrow(
                () -> new RuntimeException("User not found")
        );
        double currDistance = user.getCumulativeDistance();
        user.updatePoint((int)(currDistance % 10 + newDistance) / 10 * 100);
        user.updatePoint((int)(currDistance % 30 + newDistance) / 30 * 300);
    }

    // 연속 운동 일수에 따른 포인트 증가
    public void increasePointsByConsecutiveDate(Long userId) {
        User user = userService.findUserById(userId).orElseThrow(
                () -> new RuntimeException("User not found")
        );
        int currConsecutiveDate = user.getConsecutiveDate();
        if (currConsecutiveDate > 15) {
            currConsecutiveDate = currConsecutiveDate % 15;
        }
        switch (currConsecutiveDate) {
            case 3: user.updatePoint(100); break;
            case 7: user.updatePoint(300); break;
            case 15: user.updatePoint(500); break;
            default: break;
        }
    }

    // 당일 최초로 10분 이상 운동했을 경우 포인트 증가
    public void increasePointsForTodaysFirstExercise(Long userId) {
        List<Exercise> todaysExercises = exerciseService.getTodaysAllExercises(userId);
        User user = userService.findUserById(userId).orElseThrow(
                () -> new RuntimeException("User not found")
        );
        if (todaysExercises.isEmpty()) {
            user.updatePoint(100);
            return;
        }
        for (Exercise exercise : todaysExercises) {
            double exerciseDuration = exerciseService.calculateExerciseDuration(exercise.getStartedAt(), exercise.getCompletedAt());
            if (exerciseDuration >= 600) {
                return;
            }
        }
        user.updatePoint(100);
    }
}
