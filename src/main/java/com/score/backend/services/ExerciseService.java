package com.score.backend.services;

import com.score.backend.models.User;
import com.score.backend.models.dtos.WalkingDto;
import com.score.backend.models.exercise.Exercise;
import com.score.backend.models.exercise.ExerciseUser;
import com.score.backend.repositories.exercise.ExerciseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ExerciseService {
    private final ExerciseRepository exerciseRepository;
    private final UserService userService;
    private final LevelService levelService;

    // 유저의 당일 운동 기록 전체 조회
    public List<Exercise> getTodaysAllExercises(Long userId) {
        return exerciseRepository.findUsersExerciseToday(userId, LocalDateTime.now());
    }

    public Long saveFeed(WalkingDto walkingDto) {
        // 새로운 피드 엔티티 생성
        Exercise feed = walkingDto.toEntity();
        // 운동한 유저(피드 작성자) db에서 찾기
        User agent = userService.findUserById(walkingDto.getAgentId()).orElseThrow(
                () -> new RuntimeException("Agent not found")
        );

        // agent와 함께 운동한 유저의 id 값을 가지고 db에서 찾기
        List<ExerciseUser> exerciseUsers = new ArrayList<>();
        if (walkingDto.getOthersId() != null) {
            for (Long id : walkingDto.getOthersId()) {
                User user = userService.findUserById(id).orElseThrow(
                        () -> new RuntimeException("User not found")
                );
                exerciseUsers.add(new ExerciseUser(user));
            }
        }
        // 피드 작성자, 함께 운동한 친구 설정
        feed.setAgentAndExerciseUser(agent, exerciseUsers);
        // 피드 작성자의 마지막 운동 시간 및 날짜 설정
        updateLastExerciseDateTime(feed.getCompletedAt(), agent.getId());
        exerciseRepository.save(feed);
        return feed.getId();
    }

    public void deleteFeed(Exercise exercise) {
        exerciseRepository.delete(exercise);
    }

    @Transactional(readOnly = true)
    public Optional<Exercise> findFeedByExerciseId(Long exerciseId) {
        return exerciseRepository.findById(exerciseId);
    }

    // 운동한 시간 계산
    @Transactional(readOnly = true)
    public double calculateExerciseDuration(LocalDateTime start, LocalDateTime end) {
        Duration duration = Duration.between(start, end);
        return duration.getSeconds();
    }

    // 유저의 운동 시간 누적
    public void cumulateExerciseDuration(Long userId, LocalDateTime start, LocalDateTime end) {
        User user = userService.findUserById(userId).orElseThrow(
                () -> new RuntimeException("User not found")
        );
        user.updateCumulativeTime(calculateExerciseDuration(start, end));
    }

    // 유저의 운동 거리 누적
    public void cumulateExerciseDistance(Long userId, double distance) {
        User user = userService.findUserById(userId).orElseThrow(
                () -> new RuntimeException("User not found")
        );
        user.updateCumulativeDistance(distance);
    }

    // 유저의 연속 운동 일수 1 증가
    public void increaseConsecutiveDate(Long userId) {
        User user = userService.findUserById(userId).orElseThrow(
                () -> new RuntimeException("User not found")
        );
        List<Exercise> todaysAllExercises = getTodaysAllExercises(userId);
        if (todaysAllExercises.isEmpty()) {
            levelService.increasePointsByConsecutiveDate(userId);
            user.updateConsecutiveDate(true);
            return;
        }
        for (Exercise exercise : todaysAllExercises) {
            if (calculateExerciseDuration(exercise.getStartedAt(), exercise.getCompletedAt()) >= 600) {
                return;
            }
        }
        levelService.increasePointsByConsecutiveDate(userId);
        user.updateConsecutiveDate(true);
    }

    // 유저의 마지막 운동 시간 및 날짜 설정
    public void updateLastExerciseDateTime(LocalDateTime lastExerciseDateTime, Long userId) {
        User user = userService.findUserById(userId).orElseThrow(
                () -> new RuntimeException("User not found")
        );
        user.updateLastExerciseDateTime(lastExerciseDateTime);
    }

    // 유저의 연속 운동 일수 초기화
    @Scheduled(cron = "0 0 0 * * *")
    public void initEveryUsersConsecutiveDate() {
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.DAYS);
        List<User> userList = userService.findAll();
        for (User user : userList) {
            if (user.getLastExerciseDateTime() == null) {
                continue;
            }
            LocalDateTime lastExerciseDate = user.getLastExerciseDateTime().truncatedTo(ChronoUnit.DAYS);
            if (ChronoUnit.DAYS.between(lastExerciseDate, now) > 1) {
                user.updateConsecutiveDate(false);
            }
        }
    }
}
